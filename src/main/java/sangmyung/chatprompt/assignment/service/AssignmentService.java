package sangmyung.chatprompt.assignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.dto.*;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.IOPairs;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.dto.TaskResponse;
import sangmyung.chatprompt.task.repository.IoPairRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AssignmentService {
    private final TaskRepository taskRepository;
    private final AssignmentRepository assignRepository;





    /**
     * 교수님이 작성한 Assignment의 내용을 받아서 지시문(윤문)&기계번역문에 내용을 넣어서 반환
     * 상단에 위치할 지시문&기계번역문에 대한 내용을 반환
     * @param professorId 1L로 고정
     * @param taskId Assignment를 찾고자하는 Task PK
     */
    public TaskResponse getDefinitions(Long professorId, Long taskId){
        Task task = taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Optional<Assignment> optional = assignRepository.getAssignment(professorId, taskId);

        // 교수님이 작성하신 원문이 없는 상황 -> Task의 내용을 넣는다
        if (optional.isEmpty()){
            return convertToDefinition(new Assignment(), task);
        }

        // 교수님이 작성하신 원문이 있는 상황 -> Assignment의 내용을 전달한다
        Assignment assignment = optional.get();
        return convertToDefinition(assignment, task);
    }

    /**
     * 사용자가 이전에 작성했던 유사지시문 2개를 반환 - 만일 기존에 없었다면 새로 생성하여 전달
     * @param user 해당 내용을 작성한 사용자
     * @param taskId 내용을 작성한 Task PK
     */
    @Transactional
    public SimilarInstructResponse getWrittenSimilar(User user, Long taskId){
        Optional<Assignment> optionalAssignment = assignRepository.getAssignment(user.getId(), taskId);

        if (optionalAssignment.isEmpty()){
            return SimilarInstructResponse.builder()
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .build();
        }

        return convertToSimilar(optionalAssignment.get());
    }

    /**
     * 해당 사용자가 해당 Task에서 작성한 내용을 반환
     * 이전에 작성한 내용이 없었다면 객체 새로 생성, 변환 후 반환
     * 이전에 작성한 내용이 있다면 객체 받아와서 변환 후 반환
     * @param user 해당 내용을 작성한 사용자
     * @param taskId 내용을 작성한 Task의 PK
     */
    @Transactional
    public AssignResponse getWrittenAssignment(User user, Long taskId){
        Task task = taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Optional<Assignment> optionalAssignment = assignRepository.getAssignment(user.getId(), taskId);

        // User가 마지막으로 수정한 TaskId 갱신
        user.updateLastTaskNum(taskId);

        // 해당하는 Assignment가 없던 경우 null로 채워서 전달
        if (optionalAssignment.isEmpty()){
            return AssignResponse.builder()
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .input(null).output(null)
                    .build();
        }

        // 해당하는 Assignment가 있던 경우
        Assignment assignment = optionalAssignment.get();
        return convertToAssignResponse(assignment);
    }


    /**
     * 전달받은 내용을 통해 사용자가 수정한 내용을 변경 -> 유사지시문1,2 수정
     * @param user 수정한 사용자
     * @param taskId 사용자가 수정한 Task의 PK
     * @param assignRequest 사용자가 작성한 내용이 들어있는 객체
     */
    @Transactional
    public AssignResponse updateAssignmentContent(User user, Long taskId, AssignRequest assignRequest){
        Task task = taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        Optional<Assignment> optional = assignRepository.getAssignment(user.getId(), taskId);

        // User가 마지막으로 수정한 TaskId 갱신
        user.updateLastTaskNum(taskId);

        // 존재하지 않았던 경우 -> Assignment 객체 새로 생성
        // 이렇게 하면 버그있을거 같은데..? Entity 만들고 값 채우고 저장하고 값 전달해야하지 않을까?
        if (optional.isEmpty()){
            Assignment assignment = Assignment.builder()
                    .taskId(taskId)
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .input(null).output(null)
                    .build();

            Assignment savedAssignment = assignRepository.save(assignment);
            savedAssignment.updateSimilarInstruct(assignRequest);
            savedAssignment.addUser(user);

            return convertToAssignResponse(savedAssignment);
        }

        // Assignment가 존재했던 경우
        Assignment assignment = optional.get();
        assignment.updateSimilarInstruct(assignRequest);

        return convertToAssignResponse(assignment);
    }

    /**
     * 특정 Task에서 사용자들이 작성한 유사지시문(총 10개)를 List로 반환
     * @param taskId 사용자들이 작성한 유사지시문 쌍을 알고 싶은 Task PK
     */
    public List<SimilarInstructResponse> getTaskEverySimilar(Long taskId){
        List<SimilarInstructResponse> assignmentList = new ArrayList<>();

        // 작성한 유사지시문이 있다면 내용을 convert
        List<Assignment> assignments = assignRepository.getAssignmentList(taskId);
        for (Assignment assignment : assignments) {
            assignmentList.add(convertToSimilar(assignment));
        }

        // 10개가 안될 때 더미 데이터로 채우기
        int len = assignmentList.size();
        if (len < 10) {
            int remain = 10 - len;
            for (int i = 0; i < remain; ++i){
                assignmentList.add(SimilarInstructResponse.builder()
                        .similarInstruct1("유사 지시문1이 아직 작성되지 않았습니다.")
                        .similarInstruct2("유사 지시문2가 아직 작성되지 않았습니다.")
                        .build());
            }
        }
        return assignmentList;
    }

    /**
     *
     * @param taskId 입력한 입출력 쌍을 알고 싶은 Task의 PK
     */
    public List<AssignIOResponse> getIOPairList(Long taskId){
        Task task = taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        List<AssignIOResponse> ioList = new ArrayList<>();
        List<Assignment> ioPairList = assignRepository.getIOPairList(taskId);
        for (Assignment assignment : ioPairList) {
            ioList.add(convertToAssignIOResponse(assignment));
        }

        int remain = task.getTotalIoNum() - ioPairList.size();
        for (int i = 0; i < remain; ++i){
            ioList.add(AssignIOResponse.builder()
                            .input("입력이 아직 기입되지 않았습니다.")
                            .output("출력이 아직 기입되지 않았습니다.")
                    .build());
        }
        return ioList;
    }







    private AssignIOResponse convertToAssignIOResponse(Assignment assignment){
        return AssignIOResponse.builder()
                .input(assignment.getInput())
                .output(assignment.getOutput())
                .build();
    }
    private AssignResponse convertToAssignResponse(Assignment assignment){
        return AssignResponse.builder()
                .similarInstruct1(assignment.getSimilarInstruct1())
                .similarInstruct2(assignment.getSimilarInstruct2())
                .input(assignment.getInput())
                .output(assignment.getOutput())
                .build();
    }
    private SimilarInstructResponse convertToSimilar(Assignment assignment){
        return SimilarInstructResponse.builder()
                .similarInstruct1(assignment.getSimilarInstruct1())
                .similarInstruct2(assignment.getSimilarInstruct2())
                .build();
    }
    private TaskResponse convertToDefinition(Assignment assignment, Task task){
        String def1, def2;
        if (assignment.getSimilarInstruct1() == null || assignment.getSimilarInstruct1().equals("")){
            def1 = task.getDefinition1();
        }
        else {
            def1 = assignment.getSimilarInstruct1();
        }
        if (assignment.getSimilarInstruct2() == null || assignment.getSimilarInstruct2().equals("")){
            def2 = task.getDefinition2();
        }
        else {
            def2 = assignment.getSimilarInstruct2();
        }

        return TaskResponse.builder()
                .taskId(task.getId())
                .taskTitle(task.getTaskStr())
                .definition1(def1)
                .definition2(def2)
                .build();
    }
}
