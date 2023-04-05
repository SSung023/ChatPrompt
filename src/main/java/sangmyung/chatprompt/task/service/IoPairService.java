package sangmyung.chatprompt.task.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.dto.ValidationIOResponse;
import sangmyung.chatprompt.task.dto.ValidationResponse;
import sangmyung.chatprompt.task.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class IoPairService {
    private final AssignmentRepository assignmentRepository;
    private final TaskRepository taskRepository;


    /**
     * 특정 Task의 특정 입출력에 대해서 사용자가 검증 여부를 무엇으로 설정했는지 여부 확인
     * 0: false, 1: true
     */
    public Integer getIsIOValidated(Long userId, Long taskId, int ioIndex){
        Optional<Assignment> optional = assignmentRepository.getIOAssignment(userId, taskId, ioIndex);

        if (optional.isEmpty()){
            throw new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND);
        }

        return optional.get().getIsValidated();
    }

    /**
     * 특정 task의 특정 입출력의 검증 여부를 갱신
     */
    public ValidationResponse updateIOValidated(Long userId, Long taskId, int ioIndex, String verify){
        Optional<Assignment> optional = assignmentRepository.getIOAssignment(userId, taskId, ioIndex);
        if (optional.isEmpty()){
            throw new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND);
        }

        Assignment assignment = optional.get();

        // 검증 여부 갱신
        int isValidated = verify.equals("yes") ? 1 : 0;
        assignment.updateValidation(isValidated);

        // 특정 사용자의 특정 Task에서 검증된 입출력의 수를 얻음
        int validatedIOCnt = assignmentRepository.getValidatedIOCnt(userId, taskId);

        return ValidationResponse.builder()
                .isValidated(assignment.getIsValidated())
                .validatedCnt(validatedIOCnt)
                .build();
    }


    /**
     * 특정 사용자의 특정 Task에 대해 검증된 입출력의 수를 반환
     */
    public ValidationResponse getCurValidatedIOCnt(Long userId, Long taskId){
        int validatedIOCnt = assignmentRepository.getValidatedIOCnt(userId, taskId);

        return ValidationResponse.builder()
                .validatedCnt(validatedIOCnt)
                .build();
    }

    /**
     * 사용자가 작성한 IOPair를 모두 가져오고 검증 여부를 담아서 전달
     */
    public List<ValidationIOResponse> getIOPairValidationList(Long userId, Long assignedTaskId){
        Task task = taskRepository.findTaskByAssignedId(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        List<ValidationIOResponse> ioList = new ArrayList<>();
        List<Assignment> ioPairList = assignmentRepository.getIOPairList(userId, task.getId());
        for (Assignment assignment : ioPairList) {
            ioList.add(convertToValidIOResponse(assignment));
        }

        int remain = task.getTotalIoNum() - ioPairList.size();
        for (int i = 0; i < remain; ++i){
            ioList.add(ValidationIOResponse.builder()
                    .input(null)
                    .output(null)
                    .isValidated(false)
                    .build());
        }
        return ioList;
    }









    private ValidationIOResponse convertToValidIOResponse(Assignment assignment){
        return ValidationIOResponse.builder()
                .input(assignment.getInput())
                .output(assignment.getOutput())
                .isValidated(!(assignment.getIsValidated() == null || assignment.getIsValidated() == 0))
                .build();
    }
}
