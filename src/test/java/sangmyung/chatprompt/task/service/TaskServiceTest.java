package sangmyung.chatprompt.task.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.dto.IOResponse;
import sangmyung.chatprompt.task.dto.TaskResponse;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class TaskServiceTest {
    @Autowired TaskService taskService;

    @BeforeEach
    void beforeEach() throws JAXBException, IOException {
        String xmlPath = "prompt/preparation/sample.xml";
        taskService.parseXmlToTask(xmlPath);
    }

    @Test
    @DisplayName("PromptDTO를 Task와 IOPairs로 변환하여 저장할 수 있다.")
    public void canSaveTaskAndIOPairs() {
        //given

        //when
        List<Task> allTask = taskService.findAllTask();

        //then
        assertThat(allTask.size()).isEqualTo(13);
    }

    @Test
    @DisplayName("taskNum을 통해 특정 Task를 찾을 수 있다.")
    public void canFindTaskByTaskNum() {
        //given

        //when
        Task task = taskService.findTaskByTaskNum(683); // sample.xml에서 제일 마지막 부분

        //then
        assertThat(task.getTaskNum()).isEqualTo(683);
    }

    @Test
    @DisplayName("IOPair를 IOResponse로 변환할 수 있다.")
    public void canConvertIOPairToIOResponse() {
        //given

        //when
        List<IOResponse> taskIOPairs = taskService.getTaskIOPairs(1L);

        //then
        log.info(String.valueOf(taskIOPairs.size()));
//        assertThat(taskIOPairs.size()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("Task를 TaskResponse로 반환할 수 있다.")
    public void canConverTaskToTaskResponse(){
        //given
        Task task = Task.builder()
                .taskNum(063)
                .taskStr("task063_testTask")
                .definition_kor("definition_kor")
                .definition_eng("definition_eng")
                .build();
        Long taskId = taskService.saveTask(task);


        //when
        Task taskByPK = taskService.findTaskByPK(taskId);
        TaskResponse taskResponse = taskService.getTaskInfo(taskId);

        //then
        assertThat(taskByPK.getDefinition_kor()).isEqualTo(taskResponse.getDefinition_kor());
        assertThat(taskByPK.getDefinition_eng()).isEqualTo(taskResponse.getDefinition_eng());
    }
}







