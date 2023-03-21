package sangmyung.chatprompt.assignment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.assignment.dto.AssignRequest;
import sangmyung.chatprompt.assignment.dto.AssignResponse;
import sangmyung.chatprompt.assignment.service.AssignmentService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AssignmentController {
    private final UserService userService;
    private final AssignmentService assignmentService;


    /**
     * 사용자가 특정 Task에 작성했던 내용 요청
     * @param taskId 대상 Task의 PK
     * @param userId 내용을 받고자하는 사용자의 PK
     */
    @GetMapping("/tasks/{taskId}/users/{userId}")
    public SingleResponse<AssignResponse> getTasksAssignment(@PathVariable Long taskId, @PathVariable Long userId){
        User user = userService.findUserById(userId);
        AssignResponse assignResponse = assignmentService.getWrittenAssignment(user, taskId);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }

    /**
     * 사용자가 특정 Task에 대응되는 내용(유사지시문/입력/출력) 수정 요청: 이전에 존재하지 않았던 경우 새로 만들어서 반환
     * @param taskId 대상 Task의 PK
     * @param userId 내용을 수정하고자 하는 사용자(User)의 PK
     */
    @PatchMapping("/tasks/{taskId}/users/{userId}")
    public SingleResponse<AssignResponse> updateTasksAssignment
            (@PathVariable Long taskId, @PathVariable Long userId, @RequestBody AssignRequest assignRequest){
        User user = userService.findUserById(userId);
        AssignResponse assignResponse = assignmentService.writeAssignmentContent(user, taskId, assignRequest);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }
}
