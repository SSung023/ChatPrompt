package sangmyung.chatprompt.user.service;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.List;

@SpringBootTest
@Transactional
@Slf4j
class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Test
    @DisplayName("저장되어 있는 사용자 리스트를 받아올 수 있다.")
    public void canGetParticipantList(){
        //given
        // spring 시작 시 sql문이 실행되어 자동으로 6명의 사용자가 DB에 등록된다.
        
        //when
        List<User> userList = userService.findAllUserList();

        //then
        Assertions.assertThat(userList.size()).isEqualTo(6);
    }

    @Test
    @DisplayName("저장되어 있는 사용자를 PK를 통해 받아올 수 있다.")
    public void canGetUserById(){
        //given
        User user = addUser("ABC", "홍길동");

        //when
        User userById = userService.findUserById(user.getId());

        //then
        Assertions.assertThat(user.getName()).isEqualTo(userById.getName());
        Assertions.assertThat(user.getIdentifier()).isEqualTo(userById.getIdentifier());
    }









    private User addUser(String identifier, String name){
        return userRepository.save(User.builder()
                        .identifier(identifier)
                        .name(name)
                .build());
    }
}