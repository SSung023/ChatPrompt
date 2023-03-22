package sangmyung.chatprompt.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.service.AssignmentService;
import sangmyung.chatprompt.task.domain.IOPairs;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.dto.DefRequest;
import sangmyung.chatprompt.task.dto.IOResponse;
import sangmyung.chatprompt.task.dto.SingleIOResponse;
import sangmyung.chatprompt.task.dto.TaskResponse;
import sangmyung.chatprompt.task.repository.IoPairRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;
import sangmyung.chatprompt.xml.DTO.PromptDTO;
import sangmyung.chatprompt.xml.DTO.PromptListDTO;
import sangmyung.chatprompt.xml.service.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final IoPairRepository ioPairRepository;
    private final XmlParser xmlParser;




    // Task 엔티티를 DB에 저장
    @Transactional
    public Long saveTask(Task task){
        Task save = taskRepository.save(task);
        return save.getId();
    }

    // TaskId(PK)를 통해 Task 엔티티를 반환
    public Task findTaskByPK(Long taskId){
        return taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
    }

    // TaskNum(ex 179)를 통해 Task 엔티티를 반환
    public Task findTaskByTaskNum(int taskNum){
        return taskRepository.findTaskByTaskNum(taskNum)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
    }

    // 등록된 모든 Task들을 반환
    public List<Task> findAllTask(){
        return taskRepository.findAll();
    }

    /**
     * Task ID(PK)에 대응되는 IOPair 객체들을 반환
     * @param taskId 입출력 쌍들을 찾고자하는 Task의 PK
     */
    public List<IOResponse> getTaskIOPairs(Long taskId){
        List<IOPairs> pairsByTaskId = ioPairRepository.findPairsByTaskId(taskId);
        return pairsByTaskId.stream()
                .map(p -> convertToIOResponse(p))
                .toList();
    }

    /**
     * Task ID(PK)에 대응되는 Task의 정보(Definition)가 담긴 객체를 반환
     * userId=1&taskId에 해당하는 Assignment의 definition1&definition2를 반환
     * 없는 경우에는 Task의 정보로 반환
     * @param taskId 정보를 찾고자하는 Task의 PK
     */
    public TaskResponse getTaskDefinition(User user, Long taskId){
        Task task = findTaskByPK(taskId);

        return convertToTaskResponse(user, task);
    }

    /**
     * 사용자가 마지막으로 수정한 Task의 PK를 반환
     * @param user 사용자
     */
    public Long getLastModifiedTaskId(User user){
        return user.getLastTaskNum();
    }


    /**
     * 특정 Task의 특정 입출력 쌍을 반환
     * @param taskId 대상 Task PK
     * @param ioIndex 알고자하는 입출력 쌍의 인덱스 (ex: 1a, 1b에서의 1)
     */
    public SingleIOResponse getCertainIOPairs(Long taskId, int ioIndex){
        Task task = taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        IOPairs ioPairs = ioPairRepository.findPairByIoIndex(taskId, ioIndex)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        return convertToSingleIOResponse(task, ioPairs);
    }







    // xml 파일을 promptDTO로 변환 후, Task&IOPairs 테이블에 정보 저장 : 매우 오래 걸리기 때문에 초기에 한 번만
    @Transactional
    public void parseXmlToTask(String path) throws JAXBException, IOException {
        // xml에서 추출한 DTO를 통해 엔티티에 정보를 저장
        PromptListDTO promptList = xmlParser.unmarshall(path);
        List<PromptDTO> infoList = promptList.getInfoList();
        int len = infoList.size();

        for (int i = 0; i < len; i += 2){
            PromptDTO engDTO = infoList.get(i);
            PromptDTO korDTO = infoList.get(i+1);

            int taskNum = Integer.parseInt(engDTO.getTask().replaceAll("[^0-9]", ""));

            Optional<Task> optionalTask = taskRepository.findTaskByTaskNum(taskNum);
            Task task;
            if (optionalTask.isEmpty()){ // 만일 비어있다면 Task 객체를 생성
                task = Task.builder()
                        .taskStr(engDTO.getTask())
                        .taskNum(taskNum)
                        .category(engDTO.getCategory())
                        .definition1(engDTO.getDefinition())
                        .definition2(korDTO.getDefinition())
                        .type(engDTO.getType())
                        .numInputTokens(engDTO.getInputToken())
                        .build();
                task = taskRepository.save(task);
            }
            else {
                task = optionalTask.get();
            }

            int idx = Integer.parseInt(infoList.get(i).getIndex().replaceAll("[^0-9]", ""));

            IOPairs ioPairs = IOPairs.builder()
                    .idx(idx)
                    .input1(engDTO.getInputStr())
                    .input2(korDTO.getInputStr())
                    .output1(engDTO.getOutputStr())
                    .output2(korDTO.getOutputStr())
                    .build();
            IOPairs savedIoPair = ioPairRepository.save(ioPairs);
            savedIoPair.addTask(task);

//            log.info("task 처리" + task.getTaskNum() + "\t" + "현재 인덱스: " + i + "\t 전체 인덱스: " + len);

        }
        log.info("task 처리 완료");
    }




    private TaskResponse convertToTaskResponse(User user, Task task){
        return TaskResponse.builder()
                .taskId(task.getId())
                .definition1(task.getDefinition1())
                .definition2(task.getDefinition2())
                .hasNext(checkHasNext(user, task.getId()))
                .hasPrevious(checkHasPrevious(user, task.getId()))
                .build();
    }
    private IOResponse convertToIOResponse(IOPairs ioPairs){
        return IOResponse.builder()
                .taskId(ioPairs.getTask().getId())
                .index(ioPairs.getIdx())
                .input1(ioPairs.getInput1())
                .input2(ioPairs.getInput2())
                .output1(ioPairs.getOutput1())
                .output2(ioPairs.getOutput2())
                .build();
    }
    private SingleIOResponse convertToSingleIOResponse(Task task, IOPairs ioPairs){
        return SingleIOResponse.builder()
                .ioResponse(convertToIOResponse(ioPairs))
                .hasNext(task.getTotalIoNum() > ioPairs.getIdx()) // totalNum(120) >
                .hasPrevious(ioPairs.getIdx() <= 1) // 1 이하면 더 없음
                .build();
    }
    // 현재 Task의 Id를 기준으로 사용자가 뒤에 할당받은 Task가 더 있는지 여부 반환
    private boolean checkHasNext(User user, Long taskId){
        int endTaskIdx = user.getTaskEndIdx(); // end: 120 taskId: 13
        return endTaskIdx > taskId;
    }
    // 현재 Task의 Id를 기준으로 사용자가 앞에 할당받은 Task가 더 있는지 여부 반환
    private boolean checkHasPrevious(User user, Long taskId){
        int startTaskIdx = user.getTaskStartIdx(); // start: 1 taskId: 32
        return startTaskIdx < taskId;
    }
}
