package sangmyung.chatprompt.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.List;

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
        return userRepository.findUserByName(username);
    }

    // 현재 등록되어 있는 모든 사용자들을 반환
    public List<User> findAllUserList(){
        return userRepository.findAll();
    }
}
