package sangmyung.chatprompt.xml.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.List;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles({"test"})
@Transactional
@Slf4j
class XmlExtractorTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired XmlExtractor xmlExtractor;


    @Test
    @DisplayName("유사지시문 추출 테스트")
    public void extractInstructText(){
        //given
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        int taskPK = 7;

        //when
        Long assignedTaskId = taskRepository.findTaskAssignedId(Long.valueOf(taskPK)).get();

        User assignedUser = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedTaskId))
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        List<Assignment> written = assignmentRepository.getWrittenAssignList(assignedUser.getId(), Long.valueOf(taskPK), pageRequest);

        //then
        Assertions.assertThat(written.size()).isEqualTo(10);

    }
}