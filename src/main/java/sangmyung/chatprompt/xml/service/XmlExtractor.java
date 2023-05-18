package sangmyung.chatprompt.xml.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.IOPairs;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.repository.IoPairRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;
import sangmyung.chatprompt.xml.DTO.IOPairDTO;
import sangmyung.chatprompt.xml.DTO.IOPairListDTO;
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
    private final IoPairRepository ioPairRepository;
    private final AssignmentRepository assignmentRepository;


    /**
     * 지시문 유사발화 최종 파일 추출
     */
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

            // assignedTaskId를 통해 할당받은 user가 누구인지 확인
            User assignedUser = userRepository.findAssignedUserByTaskId(Math.toIntExact(task.getAssignedTaskId()))
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


    /**
     * 입출력 최종 파일 추출
     */
    public void extractIOPair(Pageable pageable) throws JAXBException {
//        InstructListDTO instructList = extractInstruct1List(pageable);
        IOPairListDTO ioPairList = extractIOPairListDTO(pageable);


        JAXBContext jaxbContext = JAXBContext.newInstance(IOPairListDTO.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        // XML 출력 형식을 설정합니다.
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        // 자바 객체를 XML로 마샬링합니다.
        jaxbMarshaller.marshal(ioPairList, new File("file-io.xml"));
    }

    private IOPairListDTO extractIOPairListDTO(Pageable pageable) {
        List<IOPairDTO> ioPairList = new ArrayList<>();

        int lineCnt = 0;
        // 1번부터 120까지의 Task에 해당하는 입출력을 추출
        for(int i = 1; i <= 120; ++i) {
            Task task = taskRepository.findTaskByPK(Long.valueOf(i))
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

            Long assignedId = taskRepository.findTaskAssignedId(task.getId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
            User user = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedId))
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));


            PageRequest pageable2 = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "idx"));
            List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(task.getId(), pageable2);

            // Task에 해당하는 입출력 60개를 얻은 후, IOPairDTO로 변환
            List<Assignment> assignList = assignmentRepository.getIOPairList(user.getId(), task.getId(), pageable);
            for (int idx = 1; idx <= 60; ++idx) {
                IOPairs pair;
                if (pairs.size() < idx){
                    pair = IOPairs.builder()
                            .input1(" ")
                            .output1(" ")
                            .build();
                }
                else {
                    pair = pairs.get(idx - 1);
                }

                ioPairList.add(convertToIOPairEng(task, assignList.get(idx-1), pair, ++lineCnt));
                ioPairList.add(convertToIOPairKor(task, assignList.get(idx-1), ++lineCnt));
            }
        }

        return new IOPairListDTO(ioPairList);
    }

    private IOPairDTO convertToIOPairEng(Task task, Assignment assignment, IOPairs ioPairs, int lineCnt) {
        String lineNum = lineCnt+ "a";
        String category = task.getCategory();
        String taskName = task.getTaskStr();
        String idx = assignment.getIoPairsIdx() + "a";
        String type = task.getType();
        String definition = task.getDefinition1();
        String input = ioPairs.getInput1();
        String output = ioPairs.getOutput1();

        return IOPairDTO.builder()
                .lineNum(lineCnt + "a")
                .category(task.getCategory())
                .taskName(task.getTaskStr())
                .idx(assignment.getIoPairsIdx() + "a")
                .type(task.getType())
                .definition(task.getDefinition1())
                .input(ioPairs.getInput1())
                .output(ioPairs.getOutput1())
                .build();
    }
    private IOPairDTO convertToIOPairKor(Task task, Assignment assignment, int lineCnt){
        return IOPairDTO.builder()
                .lineNum(lineCnt + "b")
                .category(task.getCategory())
                .taskName(task.getTaskStr())
                .idx(assignment.getIoPairsIdx() + "b")
                .type(task.getType())
                .definition(task.getDefinition2())
                .input(assignment.getInput())
                .output(assignment.getOutput())
                .build();
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
