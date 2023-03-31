package sangmyung.chatprompt.assignment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.ListResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.assignment.dto.*;
import sangmyung.chatprompt.assignment.service.AssignmentService;
import sangmyung.chatprompt.task.service.TaskService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AssignmentController {
    private final UserService userService;
    private final TaskService taskService;
    private final AssignmentService assignmentService;


    /**
     * 사용자가 특정 Task에 작성했던 내용 요청 - 유사지시문1, 유사지시문2, 입력, 출력
     * @param taskId 대상 Task의 PK
     * 이전 api: /api/tasks/{taskId}/assignment
     */
    @GetMapping("/tasks/{taskId}/assignment/{taskSubIdx}")
    public SingleResponse<AssignResponse> getTasksAssignment(HttpServletRequest request,
                                                             @PathVariable Long taskId, @PathVariable Long taskSubIdx){

        // Session에서 User의 정보를 얻음
        Long userId = userService.getUserIdFromSession(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        User user = userService.findUserById(userId);
        AssignResponse assignResponse = assignmentService.getWrittenAssignment(user, taskId, taskSubIdx);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }

    /**
     * 사용자가 특정 Task에 대응되는 내용(유사지시문 1&2) 수정 요청: 이전에 존재하지 않았던 경우 null로 채워서 반환
     * @param taskId 대상 Task의 PK
     * 이전 api: /api/tasks/{taskId}/assignment
     */
    @PatchMapping("/tasks/{taskId}/assignment/{taskSubIdx}")
    public SingleResponse<AssignResponse> updateTasksAssignment (HttpServletRequest request, @RequestBody AssignRequest assignRequest,
                                                                 @PathVariable Long taskId, @PathVariable Long taskSubIdx){
        // Session에서 User의 정보를 얻음
        Long userId = userService.getUserIdFromSession(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        User user = userService.findUserById(userId);
        AssignResponse assignResponse = assignmentService.updateAssignmentContent(user, taskId, taskSubIdx, assignRequest);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }

    /**
     * 특정 Task 내의 특정 입출력 쌍에 해당하는 입출력 데이터를 입력(갱신) 요청 -> 입력 편집 페이지
     * @param taskId Task PK
     * @param ioIndex 특정 Task 내 입출력 쌍의 인덱스
     * 이전 api: /api/tasks/{taskId}/assignment/{ioIndex}
     */
    @PatchMapping("/tasks/{taskId}/assignment-io/{ioIndex}")
    public SingleResponse<AssignIOResponse> updateTaskIOAssignment(HttpServletRequest request, @PathVariable Long taskId,
                                                                   @PathVariable int ioIndex, @RequestBody AssignIORequest assignIORequest){
        // Session에서 User의 정보를 얻음
        Long userId = userService.getUserIdFromSession(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        AssignIOResponse assignIOResponse = taskService.updateIOAssignmentContent(userId, taskId, ioIndex, assignIORequest);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignIOResponse);
    }

    /**
     * 특정 Task 내에 작성했던 특정 입출력 쌍 반환 요청 -> 입력 편집 페이지
     * @param taskId Task PK
     * @param ioIndex 특정 Task 내 입출력 쌍의 인덱스
     * 이전 api: /api/tasks/{taskId}/assignment/{ioIndex}
     */
    @GetMapping("/tasks/{taskId}/assignment-io/{ioIndex}")
    public SingleResponse<AssignIOResponse> getTaskIOAssignment(HttpServletRequest request,
                                                                @PathVariable Long taskId, @PathVariable int ioIndex){
        // Session에서 User의 정보를 얻음
        Long userId = userService.getUserIdFromSession(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        AssignIOResponse assignIOResponse = taskService.getWrittenIOAssignContent(userId, taskId, ioIndex);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignIOResponse);
    }



    /**
     * 특정 Task에서 사용자가 작성한 유사 지시문 1을 반환 -> '입력 편집' 페이지에서 지시문1로 표현됨
     * @param taskId 유사 지시문 1&2를 가지고 오고자 하는 Task의 PK
     * 이전 api: /api/tasks/{taskId}/assignment-similar
     */
//    @GetMapping("/tasks/{taskId}/io-definitions")
//    public SingleResponse<SimilarInstructResponse> getSimilarInstruct(HttpServletRequest request, @PathVariable Long taskId){
//        // Session에서 User의 정보를 얻음
//        Long userId = userService.getUserIdFromSession(request);
//        if (userId == null){
//            throw new BusinessException(ErrorCode.NO_AUTHORITY);
//        }
//
//        User user = userService.findUserById(userId);
//        SimilarInstructResponse similarInstructs = assignmentService.getWrittenSimilar(user, taskId);
//
//        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), similarInstructs);
//    }


    /**
     * 특정 Task에 대해 사람들이 작성한 유사지시문(총 10개)를 반환 - 관리자 용도
     * @param taskId 사람들이 작성한 유사지시문 리스트를 얻고 싶은 테스크의 할당받은 인덱스
     */
    @GetMapping("/tasks/{taskId}/assignment-similar/lists")
    public ListResponse<SingleInstructResponse> getSimilarInstructList(@PathVariable Long taskId){
        List<SingleInstructResponse> similarList = assignmentService.getWrittenTaskSimilar(taskId);

        return new ListResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), similarList);
    }

    /**
     * 특정 Task에 대해 작성한 입출력 리스트들을 반환
     * @param taskId 사람들이 작성한 입출력 리스트를 얻고 싶은 테스크의 할당받은 인덱스
     * 이전 api: /api/tasks/{taskId}/assignment/io-lists
     */
    @GetMapping("/tasks/{taskId}/assignment-io/lists")
    public ListResponse<AssignIOResponse> getIOPairList(@PathVariable Long taskId){
        List<AssignIOResponse> ioPairList = assignmentService.getIOPairList(taskId);

        return new ListResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), ioPairList);
    }
}
