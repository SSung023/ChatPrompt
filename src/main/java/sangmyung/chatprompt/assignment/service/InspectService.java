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
        List<Integer> originDupliList = new ArrayList<>();

        // 검사하고자 하는 Task의 PK를 찾음
        Long taskId = taskRepository.findTaskPK(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        // 특정 Task에서 특정 사용자가 작성했던 유사지시문들을 받음
        List<Assignment> instructList = assignmentRepository.getAssignmentList(userId, taskId);

        // 비교 대상인 유사 지시문 추출 & 세 단락으로 나누기
        String originInstruct = instructList.get(subIndex).getSimilarInstruct1();
        List<String> separatedOrigin = separateInstruct(originInstruct);

        // 나머지 다른 유사지시문(9)개와 비교해서 같은 부분이 있는지 확인
        for (Assignment assignment : instructList) {
            // target 유사지시문이 아닌 경우 세 단락이 같은지 여부를 확인
            if (!assignment.getTaskSubIdx().equals((long) subIndex)) {
                DuplicateResponse duplicateResponse = compareInstruct(originDupliList, separatedOrigin, assignment);

                inspectResponse.getDuplicatedList().add(duplicateResponse);
            }
        }
        inspectResponse.setOriginDupliIdx(originDupliList);
        return inspectResponse;
    }

    /**
     * 3조각으로 자른 두 유사지시문의 겹치는 부분을 검사
     * 3단락으로 나눈 두 유사지시문을 받아서 같은지 확인 후, 같은 부분이 있다면
     */
    public DuplicateResponse compareInstruct(List<Integer> originDupliList,
                                              List<String> origin, Assignment targetAssignment){
        Set<Integer> originSet = new HashSet<>(); // origin의 중복 인덱스
        Set<Integer> duplicatedSet = new HashSet<>(); // 비교 대상의 중복 인덱스

        DuplicateResponse duplicateResponse = new DuplicateResponse();
        List<SingleDuplicate> partList = new ArrayList<>(); // DuplicateResponse에 들어갈

        // 비교 대상을 세 단락으로 나눔
        List<String> target = separateInstruct(targetAssignment.getSimilarInstruct1());

        for(int i = 0; i < 3; ++i){
            String originInstruct = origin.get(i); // 검사 대상(origin) 단락
            for(int k = 0; k < 3; ++k){
                String targetInstruct = target.get(k); // 비교지시문(target) 단락
                int startIdx = originInstruct.length() < targetInstruct.length()
                        ? targetInstruct.indexOf(originInstruct) : originInstruct.indexOf(targetInstruct);

                // 겹치는 부분이 있는 경우 겹치는 인덱스를 저장
                if (startIdx != -1){
                    if (!originDupliList.contains(i))
                        originSet.add(i);
                    duplicatedSet.add(k);
                }
            }
        }

        // 겹치는 부분이 있다면 돌면서 SingleDuplicate List에 추가
        if (duplicatedSet.size() > 0){
            for(int i = 0; i < 3; ++i){
                partList.add(SingleDuplicate.builder()
                                .partIdx(i)
                                .partStr(target.get(i))
                                .isDuplicated(duplicatedSet.contains(i))
                        .build());
            }
        }
        duplicateResponse.setDuplicatedIdx(targetAssignment.getTaskSubIdx());
        duplicateResponse.setPartList(partList);

        return duplicateResponse;
    }

    // 단일 유사지시문을 세 단락으로 나누는 작업
    public List<String> separateInstruct(String instruct){
        List<String> instructList = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        List<Integer> cutSize = calculateCutSize(instruct);
        List<Integer> startIdx = calculateStartIdx(cutSize);

        for(int i = 0; i < 4; ++i){
            int start = startIdx.get(i);
            tempList.add(instruct.substring(start, start + cutSize.get(i)));
        }

        for(int i = 0; i < 3; ++i){
            instructList.add(tempList.get(i) + tempList.get(i + 1));
        }
        return instructList;
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
