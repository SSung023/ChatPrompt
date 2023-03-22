package sangmyung.chatprompt.assignment.service;

import lombok.RequiredArgsConstructor;
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

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
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
            return AssignResponse.builder()
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .input(null).output(null)
                    .build();

        }

        // Assignment가 존재했던 경우
        Assignment assignment = optional.get();

        // 현재 둘 중 한 쪽만 업데이트하면 다른 한 쪽이 사라지는 버그 존재 -> 수정 필요
        assignment.updateSimilarInstruct(assignRequest.getSimilarInstruct1(), assignRequest.getSimilarInstruct2());
//        assignment.updateIO(assignRequest.getInput(), assignRequest.getOutput());

        return convertToAssignResponse(assignment);
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
        if (assignment.getSimilarInstruct1() == null){
            def1 = task.getDefinition1();
        }
        else {
            def1 = assignment.getSimilarInstruct1();
        }
        if (assignment.getSimilarInstruct2() == null){
            def2 = task.getDefinition2();
        }
        else {
            def2 = assignment.getSimilarInstruct2();
        }

        return TaskResponse.builder()
                .definition1(def1)
                .definition2(def2)
                .build();
    }
}
