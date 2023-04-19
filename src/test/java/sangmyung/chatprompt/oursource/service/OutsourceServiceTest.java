package sangmyung.chatprompt.oursource.service;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles({"test"})
@Transactional
@Slf4j
class OutsourceServiceTest {
    @Autowired UserRepository userRepository;
    @Autowired TaskRepository taskRepository;


    @Test
    @DisplayName("Task PK를 통해 할당된 사용자 확인할 수 있다.")
    public void findAssignedUserByTaskPK(){
        //given
        Long taskPK = taskRepository.findTaskPK(13L)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        //when
        int assignedTaskId = 13;
        User user = userRepository.findAssignedUserByTaskId(assignedTaskId).get();

        //then
        assertThat(user.getId()).isEqualTo(6);
    }
}