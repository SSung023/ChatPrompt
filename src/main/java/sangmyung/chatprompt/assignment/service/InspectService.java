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
        InspectResponse inspectResponse = new InspectResponse();

        // 검사하고자 하는 Task의 PK를 찾음
        Long taskId = taskRepository.findTaskPK(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        // 특정 Task에서 특정 사용자가 작성했던 유사지시문들을 받음
        List<Assignment> instructList = assignmentRepository.getAssignmentList(userId, taskId);

        // 비교 대상인 유사 지시문 추출 & 세 단락으로 나누기
        String originInstruct = instructList.get(subIndex).getSimilarInstruct1();
        List<String> partList = separatePart(originInstruct);
        List<String> sectionList = separateSection(partList);

        for (String section : sectionList) {

        }


        return inspectResponse;
    }

    public SingleDuplicate compareSectionWithSingleAssignment(String section, Assignment assignment){
        String targetInstruct = assignment.getSimilarInstruct1();
        List<String> partList = separatePart(targetInstruct);
        List<String> sectionList = separateSection(partList);

        // targetInstruction의 모든 section과
        for (int i = 0; i < 3; ++i) {

        }


        return new SingleDuplicate();
    }




    // 단일 유사지시문을 세 단락으로 나누는 작업
    public List<String> separateSection(String instruct){
        List<String> partList = separatePart(instruct);
        List<String> instructList = new ArrayList<>();

        for(int i = 0; i < 3; ++i){
            instructList.add(partList.get(i) + partList.get(i + 1));
        }
        return instructList;
    }
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
}
