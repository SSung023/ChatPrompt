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
import sangmyung.chatprompt.assignment.dto.InspectResponse;
import sangmyung.chatprompt.assignment.dto.SingleDuplicate;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        List<String> partList = inspectService.separatePart(targetInstruct);
        List<String> separatedOrigin = inspectService.separateSection(partList);

        //when

        //then
        Assertions.assertThat(targetInstruct).isEqualTo(instructList.get(2).getSimilarInstruct1());
        log.info(targetInstruct);
        for (String s : separatedOrigin) {
            log.info(s);
        }
    }

    @Test
    @DisplayName("origin section과 Assignment를 비교했을 때")
    public void compareWithAssignmentTest(){
        //given
        Assignment assignment = assignmentRepository.getSubIdxAssignment(3L, 1L, 1L).get();
        String originInstruct = assignment.getSimilarInstruct1();

        //when
        List<String> partList = inspectService.separatePart(originInstruct);
        List<String> sectionList = inspectService.separateSection(partList);

        SingleDuplicate res1 = inspectService.compareSectionWithSingleAssignment(sectionList.get(0), assignment);
        SingleDuplicate res2 = inspectService.compareSectionWithSingleAssignment(sectionList.get(1), assignment);
        SingleDuplicate res3 = inspectService.compareSectionWithSingleAssignment(sectionList.get(2), assignment);

        SingleDuplicate res4 = inspectService.compareSectionWithSingleAssignment(sectionList.get(0) + sectionList.get(2), assignment);

        //then
        Assertions.assertThat(res1.getPartIdx()).contains(0, 1);
        Assertions.assertThat(res2.getPartIdx()).contains(1, 2);
        Assertions.assertThat(res3.getPartIdx()).contains(2, 3);
    }

    @Test
    @DisplayName("유사지시문 비교 테스트")
    public void instructCompareTest(){
        //given


        //when
        InspectResponse inspectResponse = inspectService.compareWithOtherInstruct(3L, 1L, 1);

        //then
        log.info(inspectResponse.toString());
    }






    private void testList(List<Integer> list){
        list.add(1);
    }
}