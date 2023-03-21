package sangmyung.chatprompt.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.task.service.TaskService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.dto.UserRequest;
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
    private final TaskService taskService;



    /**
     * 특정 사용자 선택과 함께, 사용자가 마지막으로 수정한 Task의 PK를 반환
     * @param userRequest 사용자의 실명이 담긴 DTO
     */
    @PostMapping("/login")
    public SingleResponse<Long> userLogin(HttpServletRequest request, @RequestBody UserRequest userRequest){
        // 세션 생성
        HttpSession session = request.getSession();

        String username = userRequest.getUsername(); // 실명이 담긴 객체
        User user = userService.findUserByUserName(username);

        Long lastModifiedTaskId = taskService.getLastModifiedTaskId(user);

        // session에 User의 정보(PK)를 담아서 전달 -> 사용자 파악에 사용
        session.setAttribute(LOGIN_MEMBER_PK, user.getId());

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), lastModifiedTaskId);
    }

    @PostMapping("/logout")
    public CommonResponse userLogout(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        // session 존재하는 경우 session 제거
        if (session != null){
            session.invalidate();
        }

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }
}
