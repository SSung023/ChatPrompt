package sangmyung.chatprompt.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.task.domain.IOPairs;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.dto.DefRequest;
import sangmyung.chatprompt.task.dto.IOResponse;
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
    private final UserRepository userRepository;
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
     * @param taskId 정보를 찾고자하는 Task의 PK
     */
    public TaskResponse getTaskDefinition(Long taskId){
        Task task = findTaskByPK(taskId);
        return convertToTaskResponse(task);
    }

    /**
     * 사용자가 마지막으로 수정한 Task의 PK를 반환
     * @param username 사용자의 실명
     */
    public Long getLastModifiedTaskId(String username){
        User user = userRepository.findUserByName(username);
        return user.getLastTaskNum();
    }


    /**
     * Task(1~120)의 지시문(definition_eng)의 등록(변경) 요청
     * @param taskId 지시문을 변경하고자 하는 Task의 PK
     * @param userId 지시문을 변경하고자 하는 사용자의 PK
     * @param defRequest 변경하고자 하는 지시문의 내용
     */
    @Transactional
    public TaskResponse updateDefinition(Long taskId, Long userId, DefRequest defRequest){
        Task task = taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        if (userId == 1 || userId == 2 || userId == 3){
            String newDefinition = defRequest.getNewDefinition();
            task.updateDefinition(newDefinition);
        }

        return convertToTaskResponse(task);
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
                        .instruction(engDTO.getDefinition())
                        .definition_kor(korDTO.getDefinition())
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

            log.info("task 처리" + task.getTaskNum() + "\t" + "현재 인덱스: " + i + "\t 전체 인덱스: " + len);

        }
        log.info("task 처리 완료");
    }




    private TaskResponse convertToTaskResponse(Task task){
        return TaskResponse.builder()
                .taskId(task.getId())
                .instruction(task.getInstruction())
                .definition_kor(task.getDefinition_kor())
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

}
