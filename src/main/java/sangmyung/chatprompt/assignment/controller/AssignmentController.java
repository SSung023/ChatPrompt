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


    @GetMapping("/tasks/{taskId}/users/{userId}")
    public SingleResponse<AssignResponse> getTasksAssignment(@PathVariable Long taskId, @PathVariable Long userId){
        User user = userService.findUserById(userId);
        AssignResponse assignResponse = assignmentService.getWrittenAssignment(user, taskId);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }

    @PatchMapping("/tasks/{taskId}/users/{userId}")
    public SingleResponse<AssignResponse> updateTasksAssignment
            (@PathVariable Long taskId, @PathVariable Long userId, @RequestBody AssignRequest assignRequest){
        User user = userService.findUserById(userId);
        AssignResponse assignResponse = assignmentService.writeAssignmentContent(user, taskId, assignRequest);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }
}
