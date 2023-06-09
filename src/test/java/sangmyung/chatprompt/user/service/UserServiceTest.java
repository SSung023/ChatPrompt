package sangmyung.chatprompt.user.service;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles({"test"})
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
        User user1 = addUser("ABC1", "홍길동1");
        User user2 = addUser("ABC2", "홍길동2");
        User user3 = addUser("ABC3", "홍길동3");
        
        //when
        List<User> userList = userService.findAllUserList();

        //then
        assertThat(userList.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("저장되어 있는 사용자를 PK를 통해 받아올 수 있다.")
    public void canGetUserById(){
        //given
        User user = addUser("ABC", "홍길동");

        //when
        User userById = userService.findUserById(user.getId());

        //then
        assertThat(user.getName()).isEqualTo(userById.getName());
        assertThat(user.getIdentifier()).isEqualTo(userById.getIdentifier());
    }
    
    @Test
    @DisplayName("저장되어 있는 사용자를 이름을 통해 받아올 수 있다.")
    public void canGetUserByUserName(){
        //given
        User user1 = addUser("ABC", "홍길동");
        
        //when
        User userByUserName = userService.findUserByUserName(user1.getName());

        //then
        assertThat(user1.getName()).isEqualTo(userByUserName.getName());
        assertThat(user1.getIdentifier()).isEqualTo(userByUserName.getIdentifier());
    }

    @Test
    @DisplayName("사용자를 이름과 구분자를 통해 찾을 수 있다.")
    public void canFindUserBoth(){
        //given
        User user = addUser("ABC", "홍길동");

        //when
        User foundUser = userService.findRegisteredUser(user.getName(), user.getIdentifier());

        //then
        assertThat(user.getName()).isEqualTo(foundUser.getName());
        assertThat(user.getIdentifier()).isEqualTo(foundUser.getIdentifier());

    }

    @Test
    @DisplayName("사용자가 할당받은 테스크인지 여부를 확인할 수 있다.")
    public void canCheckIsAssignedTaskNum(){
        //given
        User user = addUser("A", "홍길동", 10, 20);

        //when


        //then

    }









    private User addUser(String identifier, String name){
        return userRepository.save(User.builder()
                        .identifier(identifier)
                        .name(name)
                .build());
    }
    private User addUser(String identifier, String name, int taskStartIdx, int taskEndIdx){
        return userRepository.save(User.builder()
                .identifier(identifier)
                .name(name)
                        .taskStartIdx(taskStartIdx)
                        .taskEndIdx(taskEndIdx)
                .build());
    }
}