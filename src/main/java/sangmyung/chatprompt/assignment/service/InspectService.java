package sangmyung.chatprompt.assignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.dto.DuplicateResponse;
import sangmyung.chatprompt.assignment.dto.InspectResponse;
import sangmyung.chatprompt.assignment.dto.SingleDuplicate;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class InspectService {
    private final TaskRepository taskRepository;
    private final AssignmentRepository assignmentRepository;


    /**
     * 특정 Task의 유사지시문 10개를 모두 불러온 후, 세 단락으로 나눠서 비교
     * @param userId 검사하고자 하는 사용자의 PK
     * @param assignedTaskId 검사하고자 하는 Task의 PK
     * @param subIndex 유사지시문 10개 중에 검사할 유사지시문의 subIndex
     */
    public InspectResponse compareWithOtherInstruct(Long userId, Long assignedTaskId, int subIndex) {

        // 검사하고자 하는 Task의 PK를 찾음
        Long taskId = taskRepository.findTaskPK(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        String originInstruct;
        List<Assignment> instructList = assignmentRepository.getAssignmentList(userId, taskId);

        // 특정 Task에서 교수님이 작성한 지시문
        Assignment official = assignmentRepository.extractOfficialInstruct(taskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        // official 지시문 내용 추가
        instructList.add(Assignment.builder()
                .similarInstruct1(official.getSimilarInstruct1())
                .taskSubIdx(11L)
                .build());
        instructList.add(Assignment.builder()
                .similarInstruct1(official.getSimilarInstruct2())
                .taskSubIdx(12L)
                .build());

        // official 지시문에 대해 요청이 들어온 경우
        if (subIndex == 11L || subIndex == 12L){
            originInstruct = subIndex == 11L ? official.getSimilarInstruct1() : official.getSimilarInstruct2();
        }
        // 특정 Task에서 특정 사용자가 작성했던 유사지시문들을 받음
        else {
            originInstruct = instructList.get(subIndex - 1).getSimilarInstruct1();
        }

        // 비교 대상(original)인 유사 지시문 추출 & 세 단락으로 나누기
        List<String> partList = separatePart(originInstruct);
        List<String> sectionList = separateSection(partList);

        List<DuplicateResponse> dupliList = new ArrayList<>();
        // original의 각 section(총 3개)를 돌아가면서 검사를 실시
        for (int i = 0; i < 3; ++i) {
            String section = sectionList.get(i); // original section
            List<SingleDuplicate> result = compareSectionWithAssignments(section, instructList, subIndex);

            // 한 section에 대한 response를 생성하고, InspectResponse에 추가
            dupliList.add(DuplicateResponse.builder()
                    .sectionNum(i)
                    .originSection(section)
                    .duplicates(result)
                    .build());
        }

        return InspectResponse.builder()
                .originalInstruct(originInstruct)
                .duplicateList(dupliList)
                .build();
    }

    // original instruct의 한 section과, 나머지 instruction들과 비교해서 response 객체 생성
    public List<SingleDuplicate> compareSectionWithAssignments(String originSection, List<Assignment> instructList, int subIndex){
        List<SingleDuplicate> singleDuplicates = new ArrayList<>();

        for (Assignment assignment : instructList) {
            // 검사 대상인 경우 검사 패스
            if (assignment.getTaskSubIdx() == subIndex)
                continue;

            SingleDuplicate result = compareSectionWithSingleAssignment(originSection, assignment);
            if (result.getPartIdx().size() == 0){
                continue;
            }
            singleDuplicates.add(result);
        }



        return singleDuplicates;
    }

    // original section과 하나의 Assignment를 비교
    public SingleDuplicate compareSectionWithSingleAssignment(String originSection, Assignment assignment){
        String targetInstruct = assignment.getSimilarInstruct1(); // 비교 대상 유사지시문 추출
        List<String> partList = separatePart(targetInstruct);
        List<String> sectionList = separateSection(partList); // 비교 대상 유사지시문을 section으로 추출

        Set<Integer> partNums = new HashSet<>();

        // targetInstruction의 모든 section과 original's section을 비교
        for (int sectionNum = 0; sectionNum < 3; ++sectionNum){
            String targetSection = sectionList.get(sectionNum); // 비교대상 section
            // 동일한 경우에는 part에 대한 정보를 추가
            if (originSection.equals(targetSection)){
                partNums.add(sectionNum);
                partNums.add(sectionNum + 1);
            }
        }


        return convertToSingleDuplicate(Math.toIntExact(assignment.getTaskSubIdx()), partNums, partList);
    }




    // 단일 유사지시문을 세 단락으로 나누는 작업
    public List<String> separateSection(List<String> partList){
        List<String> instructList = new ArrayList<>();

        for(int i = 0; i < 3; ++i){
            instructList.add(partList.get(i) + partList.get(i + 1));
        }
        return instructList;
    }
    public List<String> separatePart(String instruct){
        List<String> sectionList = new ArrayList<>();

        List<Integer> cutSize = calculateCutSize(instruct);
        List<Integer> startIdx = calculateStartIdx(cutSize);

        for(int i = 0; i < 4; ++i){
            int start = startIdx.get(i);
            sectionList.add(instruct.substring(start, start + cutSize.get(i)));
        }
        return sectionList;

    }
    // 유사지시문을 자를 사이즈를 계산
    private List<Integer> calculateCutSize(String instruct){
        List<Integer> cutSize = new ArrayList<>();

        int len = instruct.length();
        int singleSize = len / 4;
        int remainder = len % 4;

        for(int i = 0; i < 4 - remainder; ++i){
            cutSize.add(singleSize);
        }
        for(int i = 0; i < remainder; ++i){
            cutSize.add(singleSize + 1);
        }

        return cutSize;
    }
    // 유사지시문을 자를 때 시작 인덱스를 계산
    private List<Integer> calculateStartIdx(List<Integer> cutSize){
        List<Integer> indexes = new ArrayList<>();

        int startIdx = 0;
        indexes.add(startIdx);
        for(int i = 0; i < cutSize.size() - 1; ++i){
            startIdx += cutSize.get(i);
            indexes.add(startIdx);
        }
        return indexes;
    }

    private SingleDuplicate convertToSingleDuplicate(Integer targetSubIdx, Set<Integer> partNums, List<String> parts){
        List<Integer> partIdx = new ArrayList<>();

        for (Integer partNum : partNums) {
            partIdx.add(partNum);
        }

        return SingleDuplicate.builder()
                .targetSubIdx(targetSubIdx)
                .partList(parts)
                .partIdx(partIdx)
                .build();
    }
}
