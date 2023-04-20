package sangmyung.chatprompt.xml.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;
import sangmyung.chatprompt.xml.DTO.InstructDTO;
import sangmyung.chatprompt.xml.DTO.InstructListDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class XmlExtractor {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AssignmentRepository assignmentRepository;



    public void extractInstruct(Pageable pageable) throws JAXBException {
        InstructListDTO instructList = extractInstruct1List(pageable);

        JAXBContext jaxbContext = JAXBContext.newInstance(InstructListDTO.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // XML 출력 형식을 설정합니다.
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // 자바 객체를 XML로 마샬링합니다.
        jaxbMarshaller.marshal(instructList, new File("file.xml"));
    }


    private InstructListDTO extractInstruct1List(Pageable pageable) {
        List<InstructDTO> instructList = new ArrayList<>();

        for(int i = 1; i <= 120; ++i){
            Task task = taskRepository.findTaskByPK(Long.valueOf(i))
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
            Assignment officialInst = assignmentRepository.extractOfficialInstruct(Long.valueOf(i))
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));


            String taskStr = task.getTaskStr();
            extractOriginalDefinition(instructList, i, task);
            extractOfficialInstruct(instructList, i, taskStr, officialInst);

            User assignedUser = userRepository.findAssignedUserByTaskId(i)
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
            List<Assignment> written = assignmentRepository.getWrittenAssignList(assignedUser.getId(), Long.valueOf(i), pageable);
            for (Assignment assignment : written) {
                instructList.add(convertToInstructDTO(assignment, i, taskStr));
            }
        }

        return new InstructListDTO(instructList);
    }

    // Task의 definition(한글, 영어)를 등록
    private void extractOriginalDefinition(List<InstructDTO> instructList, int taskNum, Task task) {
        instructList.add(InstructDTO.builder()
                        .lineNum(taskNum + "a")
                        .task(task.getTaskStr())
                        .definition(task.getDefinition1())
                .build());
        instructList.add(InstructDTO.builder()
                        .lineNum(taskNum + "b")
                        .task(task.getTaskStr())
                        .definition(task.getDefinition2())
                .build());
    }
    private void extractOfficialInstruct(List<InstructDTO> instructList, int taskNum, String taskStr,
                                         Assignment assignment){
        instructList.add(InstructDTO.builder()
                .lineNum(taskNum + "b_v1")
                .task(taskStr)
                .definition(assignment.getSimilarInstruct1())
                .build());
        instructList.add(InstructDTO.builder()
                .lineNum(taskNum + "b_v2")
                .task(taskStr)
                .definition(assignment.getSimilarInstruct2())
                .build());
    }


    private InstructDTO convertToInstructDTO(Assignment assignment, int taskNum, String taskStr){
        int subIdx = (int) (assignment.getTaskSubIdx() + 2);
        return InstructDTO.builder()
                .lineNum(taskNum + "b_v" + subIdx)
                .task(taskStr)
                .definition(assignment.getSimilarInstruct1())
                .build();
    }
}
