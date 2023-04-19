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
import sangmyung.chatprompt.assignment.dto.DuplicateResponse;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles({"test"})
@Transactional
@Slf4j
class InspectServiceTest {

    @Autowired InspectService inspectService;
    @Autowired TaskRepository taskRepository;
    @Autowired AssignmentRepository assignmentRepository;


    @Test
    @DisplayName("List pass-by-ref 테스트")
    public void listPassTest(){
        // given
        List<Integer> intList = new ArrayList<>();

        // when
        testList(intList);

        // then
        Assertions.assertThat(intList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("두 문장 비교했을 때 같은 부분이 있을 때")
    public void compare(){
        //given
        List<Assignment> instructList = assignmentRepository.getAssignmentList(6L, 111L);

        String targetInstruct = instructList.get(0).getSimilarInstruct1();
        List<String> separatedOrigin = inspectService.separateInstruct(targetInstruct);

        //when
        List<Integer> originDupliList = new ArrayList<>();
        DuplicateResponse duplicateResponse = inspectService.compareInstruct(originDupliList, separatedOrigin, instructList.get(2));

        //then
        Assertions.assertThat(targetInstruct).isEqualTo(instructList.get(2).getSimilarInstruct1());
        log.info(duplicateResponse.toString());

    }






    private void testList(List<Integer> list){
        list.add(1);
    }
}