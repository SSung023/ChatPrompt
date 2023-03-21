package sangmyung.chatprompt.assignment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.dto.AssignRequest;
import sangmyung.chatprompt.assignment.dto.AssignResponse;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AssignmentService {
    private final TaskRepository taskRepository;
    private final AssignmentRepository repository;



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
        Optional<Assignment> optionalAssignment = repository.getAssignment(user.getId(), taskId);

        // User가 마지막으로 수정한 TaskId 갱신
        user.updateLastTaskNum(taskId);

        // 해당하는 Assignment가 없던 경우
        if (optionalAssignment.isEmpty()){
            Assignment assignment = Assignment.builder()
                    .taskId(taskId)
                    .similarInstruct1("").similarInstruct2("")
                    .input("").output("")
                    .build();
            Assignment savedAssign = repository.save(assignment);
            savedAssign.addUser(user);

            if (user.getId() == 1){ // 교수인 경우
                return convertToProAssignResponse(task, savedAssign);
            }
            return convertToAssignResponse(savedAssign);
        }

        // 해당하는 Assignment가 있던 경우
        Assignment assignment = optionalAssignment.get();
        if (user.getId() == 1){ // 교수인 경우
            return convertToProAssignResponse(task, assignment);
        }
        return convertToAssignResponse(assignment);
    }


    /**
     * 전달받은 내용을 통해 사용자가 수정한 내용을 변경
     * @param user 수정한 사용자
     * @param taskId 사용자가 수정한 Task의 PK
     * @param assignRequest 사용자가 작성한 내용이 들어있는 객체
     */
    @Transactional
    public AssignResponse writeAssignmentContent(User user, Long taskId, AssignRequest assignRequest){
        Task task = taskRepository.findTaskByPK(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        Optional<Assignment> optional = repository.getAssignment(user.getId(), taskId);

        // User가 마지막으로 수정한 TaskId 갱신
        user.updateLastTaskNum(taskId);

        // 존재하지 않았던 경우 -> Assignment 객체 새로 생성
        if (optional.isEmpty()){
            Assignment assignment = Assignment.builder()
                    .taskId(taskId)
                    .similarInstruct1(assignRequest.getSimilarInstruct1())
                    .similarInstruct2(assignRequest.getSimilarInstruct2())
                    .input(assignRequest.getInput())
                    .output(assignRequest.getOutput())
                    .build();
            Assignment savedAssign = repository.save(assignment);
            savedAssign.addUser(user);

            if (user.getId() == 1){ // 교수인 경우
                return convertToProAssignResponse(task, savedAssign);
            }
            return convertToAssignResponse(savedAssign);
        }

        // Assignment가 존재했던 경우
        Assignment assignment = optional.get();

        assignment.updateSimilarInstruct(assignRequest.getSimilarInstruct1(), assignRequest.getSimilarInstruct2());
        assignment.updateIO(assignRequest.getInput(), assignRequest.getOutput());

        if (user.getId() == 1) {
            return convertToProAssignResponse(task, assignment);
        }

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
    private AssignResponse convertToProAssignResponse(Task task, Assignment assignment){
        return AssignResponse.builder()
                .similarInstruct1(task.getInstruction())
                .similarInstruct2(assignment.getSimilarInstruct2())
                .input(assignment.getInput())
                .output(assignment.getOutput())
                .build();
    }
}
