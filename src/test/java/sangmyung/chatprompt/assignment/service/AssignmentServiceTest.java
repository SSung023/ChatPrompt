package sangmyung.chatprompt.assignment.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.dto.AssignRequest;
import sangmyung.chatprompt.assignment.dto.AssignResponse;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles({"test"})
@Transactional
@Slf4j
class AssignmentServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    AssignmentRepository assignmentRepository;
    @Autowired
    AssignmentService service;


    @Test
    @DisplayName("Assignment 내용이 있을 때, getWrittenAssignment를 호출하면 AssignResponse로 변환할 수 있다.")
    public void canConvertToAssignResponse1(){
        //given
        User user = getUser("username");
        Task task = getTask(1);
        Assignment assignment = getAssignment(task, user);

        //when
        AssignResponse writtenAssignment = service.getWrittenAssignment(user, task.getId());

        //then
        assertThat(writtenAssignment.getSimilarInstruct1()).isEqualTo("유사지시문1");
        assertThat(writtenAssignment.getSimilarInstruct2()).isEqualTo("유사지시문2");
        assertThat(writtenAssignment.getInput()).isEqualTo("입력");
        assertThat(writtenAssignment.getOutput()).isEqualTo("출력");

        assertThat(user.getLastTaskNum()).isEqualTo(task.getId());
    }

    @Test
    @DisplayName("Assignment 내용이 없을 때, getWrittenAssignment를 호출하면 AssignResponse로 변환할 수 있다.")
    public void canConvertToAssignResponse2(){
        //given
        User user = getUser("username");
        Task task = getTask(1);

        //when
        AssignResponse writtenAssignment = service.getWrittenAssignment(user, task.getId());

        //then
        assertThat(writtenAssignment.getSimilarInstruct1()).isEqualTo("");
        assertThat(writtenAssignment.getSimilarInstruct2()).isEqualTo("");
        assertThat(writtenAssignment.getInput()).isEqualTo("");
        assertThat(writtenAssignment.getOutput()).isEqualTo("");

        assertThat(user.getLastTaskNum()).isEqualTo(task.getId());
    }

    @Test
    @DisplayName("변경사항 저장 시도 시, 이미 있었던 객체라면 내용을 갱신한다.")
    public void canWriteContent(){
        //given
        User user = getUser("username");
        Task task = getTask(1);
        Assignment assignment = getAssignment(task, user);

        //when
        AssignRequest assignRequest = getAssignRequest("s1", "s2", "in", "out");
        AssignResponse assignResponse = service.writeAssignmentContent(user, task.getId(), assignRequest);

        //then
        assertThat(assignResponse.getSimilarInstruct1()).isEqualTo("s1");
        assertThat(assignResponse.getSimilarInstruct2()).isEqualTo("s2");
        assertThat(assignResponse.getInput()).isEqualTo("in");
        assertThat(assignResponse.getOutput()).isEqualTo("out");

        assertThat(user.getLastTaskNum()).isEqualTo(task.getId());
    }




    private User getUser(String name){
        User user = User.builder().name(name).build();
        return userRepository.save(user);
    }
    private Task getTask(int taskNum){
        Task task = Task.builder()
                .taskNum(taskNum)
                .taskStr("task" + taskNum + "_testTask")
                .instruction("instruction" + taskNum)
                .definition_kor("definition_kor" + taskNum)
                .build();

        return taskRepository.save(task);
    }
    private Assignment getAssignment(Task task, User user){
        Assignment assignment = Assignment.builder()
                .taskId(task.getId())
                .similarInstruct1("유사지시문1")
                .similarInstruct2("유사지시문2")
                .input("입력")
                .output("출력")
                .build();
        assignment = assignmentRepository.save(assignment);
        assignment.addUser(user);
        return assignment;
    }
    private AssignRequest getAssignRequest(String s1, String s2, String in, String out){
        return AssignRequest.builder()
                .similarInstruct1(s1)
                .similarInstruct2(s2)
                .input(in)
                .output(out)
                .build();
    }
}