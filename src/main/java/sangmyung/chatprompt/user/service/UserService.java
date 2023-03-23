package sangmyung.chatprompt.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.List;

import static sangmyung.chatprompt.Util.session.SessionConst.LOGIN_MEMBER_PK;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;




    // id(PK)를 통해 등록되어 있는 User 찾은 후 반환
    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
    }

    // username(실명)을 통해 User 찾은 후 반환
    public User findUserByUserName(String username){
        return userRepository.findUserByName(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
    }

    public User findUserByIdentifier(String identifier){
        return userRepository.findUserByIdentifier(identifier)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
    }

    // 현재 등록되어 있는 모든 사용자들을 반환
    public List<User> findAllUserList(){
        return userRepository.findAll();
    }


    /**
     * HttpSession에서 정보를 추출해서 UserId(PK)를 추출
     * Session 존재 X 시 강제 로그아웃 처리
     */
    public Long getUserIdFromSession(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        // session이 존재하지 않는 경우
        if (session == null){
            logoutUser(request);
            return 0L;
        }

        return (Long) session.getAttribute(LOGIN_MEMBER_PK);
    }

    public void logoutUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);

        // session 존재하는 경우 session 제거
        if (session != null){
            session.invalidate();
        }
    }
}
