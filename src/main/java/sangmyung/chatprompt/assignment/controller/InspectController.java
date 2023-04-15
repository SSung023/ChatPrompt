package sangmyung.chatprompt.assignment.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.ListResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.assignment.dto.InspectResponse;
import sangmyung.chatprompt.assignment.service.InspectService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class InspectController {
    private final InspectService inspectService;
    private final UserService userService;


    /**
     * 특정 Task에 자신이 작성한 유사지시문을 비교(표절 검사)하는 기능
     * /api/inspect/tasks/1?targetIdx=2
     */
    @GetMapping("/inspect/tasks/{taskId}")
    public SingleResponse<InspectResponse> compareInstruct(HttpServletRequest request,
                                          @PathVariable Long taskId, @RequestParam int targetIdx){
        // Session에서 User의 정보를 얻음
        Long userId = validateTaskIdxAndGetUser(request, taskId).getId();

        InspectResponse inspectResponse = inspectService.compareWithOtherInstruct(userId, taskId, targetIdx);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), inspectResponse);
    }










    private User validateTaskIdxAndGetUser(HttpServletRequest request, Long taskId) {
        // Session에서 User의 정보를 얻음
        Long userId = userService.getUserIdFromSession(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        User user = userService.findUserById(userId);

        // 사용자가 할당받은 TaskId가 아니라면 Exception 발생
        if (!userService.isAssignedTaskNum(user, taskId)){
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return user;
    }
}
