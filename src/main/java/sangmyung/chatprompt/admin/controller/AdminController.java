package sangmyung.chatprompt.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.assignment.dto.AssignRequest;
import sangmyung.chatprompt.assignment.dto.AssignResponse;
import sangmyung.chatprompt.assignment.service.AssignmentService;
import sangmyung.chatprompt.task.service.TaskService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.service.UserService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class AdminController {
    private final UserService userService;
    private final AssignmentService assignmentService;

    @GetMapping("/admin/tasks/{taskId}/assignment")
    public SingleResponse<AssignResponse> getAdminInstruct(@PathVariable Long taskId){

        User user = userService.findUserById(1L); // 어드민 계정 반환
        AssignResponse assignResponse = assignmentService.getAdminWrittenSimilar(user, taskId);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }

    @PatchMapping("/admin/tasks/{taskId}/assignment")
    public SingleResponse<AssignResponse> modifyAdminInstruct(@PathVariable Long taskId, @RequestBody AssignRequest assignRequest){
        User user = userService.findUserById(1L); // 어드민 계정 반환

        AssignResponse assignResponse = assignmentService.updateAdminWrittenSimilar(user, taskId, assignRequest);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), assignResponse);
    }
}
