package sangmyung.chatprompt.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.task.service.TaskService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.dto.UserRequest;
import sangmyung.chatprompt.user.dto.UserResponse;
import sangmyung.chatprompt.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static sangmyung.chatprompt.Util.session.SessionConst.LOGIN_MEMBER_PK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final UserService userService;



    /**
     * 특정 사용자 선택과 함께, 사용자가 마지막으로 수정한 Task의 PK를 반환
     * @param identifier 사용자의 구분자(A~)
     * /api/login?username=홍길동&identifier=A
     */
    @PostMapping("/login")
    public SingleResponse<UserResponse> userLogin(HttpServletRequest request,
                                                  @RequestParam String username, @RequestParam String identifier){
        // 세션 생성
        HttpSession session = request.getSession();

        User user = userService.findRegisteredUser(username, identifier);
//        User user = userService.findUserByIdentifier(identifier);
        UserResponse userResponse = UserResponse.builder()
                .lastModifiedTaskNum(user.getLastTaskNum())
                .taskStartIdx(user.getTaskStartIdx())
                .taskEndIdx(user.getTaskEndIdx())
                .ioStartIdx(user.getIoStartIdx())
                .ioEndIdx(user.getIoEndIdx())
                .build();

        // session에 User의 정보(PK)를 담아서 전달 -> 사용자 파악에 사용
        session.setAttribute(LOGIN_MEMBER_PK, user.getId());

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), userResponse);
    }

    @PostMapping("/logout")
    public CommonResponse userLogout(HttpServletRequest request){

        userService.logoutUser(request);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/user")
    public SingleResponse<UserResponse> getUserInfo(HttpServletRequest request){
        // Session에서 User의 정보를 얻음
        Long userId = userService.getUserIdFromSession(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        User user = userService.findUserById(userId);
        UserResponse userResponse = UserResponse.builder()
                .lastModifiedTaskNum(user.getLastTaskNum())
                .taskStartIdx(user.getTaskStartIdx())
                .taskEndIdx(user.getTaskEndIdx())
                .ioStartIdx(user.getIoStartIdx())
                .ioEndIdx(user.getIoEndIdx())
                .build();

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), userResponse);
    }
}
