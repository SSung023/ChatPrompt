package sangmyung.chatprompt.assignment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.dto.*;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.Task;
import sangmyung.chatprompt.task.dto.TaskResponse;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class AssignmentService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AssignmentRepository assignRepository;





    /**
     * 교수님이 작성한 Assignment의 내용을 받아서 지시문(윤문)&기계번역문에 내용을 넣어서 반환
     * 상단에 위치할 지시문&기계번역문에 대한 내용을 반환
     * @param professorId 1L로 고정
     * @param assignedTaskId Assignment를 찾고자하는 Task의 할당받은 아이디
     */
    public TaskResponse getDefinitions(Long professorId, Long assignedTaskId){
        Task task = taskRepository.findTaskByAssignedId(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Long taskId = task.getId();

        Optional<Assignment> optional = assignRepository.getAssignment(professorId, taskId);

        // 교수님이 작성하신 원문이 없는 상황 -> Task의 내용을 넣는다
        if (optional.isEmpty()){
            return convertToDefinition(new Assignment(), task);
        }

        // 교수님이 작성하신 원문이 있는 상황 -> Assignment의 내용을 전달한다
        Assignment assignment = optional.get();
        return convertToDefinition(assignment, task);
    }

    /**
     * 관리자(어드민) 전용 기능
     * 사용자가 이전에 작성했던 유사지시문 2개를 반환 - 만일 기존에 없었다면 null을 담아서 전달
     * @param user 해당 내용을 작성한 사용자
     * @param assignedTaskId 내용을 작성한 Task가 할당받은 번호
     */
    public AssignResponse getAdminWrittenSimilar(User user, Long assignedTaskId){
        Task task = taskRepository.findTaskByAssignedId(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Long taskId = task.getId();

        Optional<Assignment> optionalAssignment = assignRepository.getAssignment(user.getId(), taskId);

        if (optionalAssignment.isEmpty()){
            return AssignResponse.builder()
                    .taskTitle(task.getTaskStr())
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .build();
        }

        return convertToAssignResponse(task.getTaskStr(), optionalAssignment.get());
    }

    /**
     * 관리자(어드민) 전용 기능
     * 사용자가 이전에 작성했던 유사지시문 2개를 수정 - 만일 기존에 없다면 새로 생성하여 전달
     * @param user 해당 내용을 작성한 사용자(관리자)
     * @param assignedTaskId 내용을 작성한 Task가 할당받은 번호
     */
    @Transactional
    public AssignResponse updateAdminWrittenSimilar(User user, Long assignedTaskId, AssignRequest assignRequest){
        Task task = taskRepository.findTaskByAssignedId(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Long taskId = task.getId();

        Optional<Assignment> optional = assignRepository.getAssignment(user.getId(), taskId);
        if (optional.isEmpty()){
            return AssignResponse.builder()
                    .taskTitle(task.getTaskStr())
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .build();
        }
        Assignment assignment = optional.get();
        assignment.updateSimilarInstruct(assignRequest);


        return convertToAssignResponse(task.getTaskStr(), assignment);
    }


    /**
     * 해당 사용자가 해당 Task에서 작성한 내용을 반환
     * 이전에 작성한 내용이 없었다면 객체 새로 생성, 변환 후 반환
     * 이전에 작성한 내용이 있다면 객체 받아와서 변환 후 반환
     * 단, 사용자가 A인 경우에는 구축자 C~F가 작성했던 내용을 반환
     * @param user 해당 내용을 작성한 사용자
     * @param assignedTaskId 내용을 작성한 Task의 할당된 번호 -> PK 변환 필요
     */
    public AssignResponse getWrittenAssignment(User user, Long assignedTaskId, Long taskSubIdx){
        Task task = taskRepository.findTaskByAssignedId(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Long taskId = task.getId();

        Optional<Assignment> optionalAssignment = getAssginmentByIdentifier(user, assignedTaskId, taskSubIdx, taskId);
        // User가 마지막으로 수정한 TaskId 갱신
        user.updateLastTaskNum(assignedTaskId);

        // 해당하는 Assignment가 없던 경우 null로 채워서 전달
        if (optionalAssignment.isEmpty()){
            return AssignResponse.builder()
                    .taskTitle(task.getTaskStr())
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .taskSubIdx(taskSubIdx)
                    .build();
        }

        // 해당하는 Assignment가 있던 경우
        Assignment assignment = optionalAssignment.get();
        return convertToAssignResponse(task.getTaskStr(), assignment);
    }


    /**
     * 전달받은 내용을 통해 사용자가 수정한 내용을 변경 -> 유사지시문1 수정
     * @param user 수정한 사용자
     * @param assignedTaskId 사용자가 수정한 Task의 할당받은 번호 -> PK로 변환 필요
     * @param assignRequest 사용자가 작성한 내용이 들어있는 객체
     */
    @Transactional
    public AssignResponse updateAssignmentContent(User user, Long assignedTaskId, Long taskSubIdx, AssignRequest assignRequest){
        Task task = taskRepository.findTaskByAssignedId(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Long taskId = task.getId();

        // 사용자 A를 고려하여 실제 할당받은 user의 정보를 미리 받아옴
        User assignedUser = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedTaskId))
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        // 구축자 (C~F)가 작성했던 Assignment를 받아옴
        Optional<Assignment> optional = getAssginmentByIdentifier(user, assignedTaskId, taskSubIdx, taskId);

        // User가 마지막으로 수정한 Task의 번호(assignedTaskId) 수정
        user.updateLastTaskNum(assignedTaskId);

        // 존재하지 않았던 경우 -> Assignment 객체 새로 생성
        if (optional.isEmpty()){
            Assignment assignment = Assignment.builder()
                    .taskId(taskId)
                    .similarInstruct1(null)
                    .similarInstruct2(null)
                    .input(null).output(null)
                    .ioPairsIdx(null)
                    .build();

            Assignment savedAssignment = assignRepository.save(assignment);
            savedAssignment.updateSimilarInstruct(assignRequest);
            savedAssignment.addTaskSubIndex(taskSubIdx);

            if (user.getId() == 1L){
                savedAssignment.addUser(assignedUser);
            }
            else {
                savedAssignment.addUser(user);
            }

            return convertToAssignResponse(task.getTaskStr(), savedAssignment);
        }

        // Assignment가 존재했던 경우
        Assignment assignment = optional.get();
        assignment.updateSimilarInstruct(assignRequest);

        return convertToAssignResponse(task.getTaskStr(), assignment);
    }

    /**
     * 사용자가 A인 경우 -> C~F가 작성했던 Assignment 반환
     * 사용자가 C~F인 경우 -> 본인이 작성했던 Assignment 반환
     */
    private Optional<Assignment> getAssginmentByIdentifier(User user, Long assignedTaskId, Long taskSubIdx, Long taskId) {
        Optional<Assignment> optional = null;
        if (user.getIdentifier().equals("A")) {
            User assignedUser = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedTaskId))
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
            optional = assignRepository.getSubIdxAssignment(assignedUser.getId(), taskId, taskSubIdx);
        }
        else {
            optional = assignRepository.getSubIdxAssignment(user.getId(), taskId, taskSubIdx);
        }
        return optional;
    }

    /**
     * 사용자가 작성한 유사지시문 10개를 반환
     * @param assignedTaskId
     */
    public List<SingleInstructResponse> getWrittenTaskSimilar(Long userId, Long assignedTaskId, Pageable pageable){
        Long taskId = taskRepository.findTaskPK(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        List<SingleInstructResponse> assignmentList = new ArrayList<>();

        // 사용자가 등록했던 Assignment를 모두 불러옴
        List<Assignment> assignments = assignRepository.getWrittenAssignList(userId, taskId, pageable);
        return getSingleInstructList(assignmentList, assignments);
    }

    // Admin인 경우 자신이 작성하지 않은 경우에도 지시문 전체 조회에서 다른 사용자가 작성한 내역을 조회할 수 있음
    public List<SingleInstructResponse> adminGetWrittenTaskSimilar(Long assignedTaskId, Pageable pageable) {
        User assignedUser = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedTaskId))
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        Long taskId = taskRepository.findTaskPK(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        List<SingleInstructResponse> assignmentList = new ArrayList<>();

        // 사용자가 등록했던 Assignment를 모두 불러옴
        List<Assignment> assignments = assignRepository.getWrittenAssignList(assignedUser.getId(), taskId, pageable);
        return getSingleInstructList(assignmentList, assignments);
    }
    private List<SingleInstructResponse> getSingleInstructList(List<SingleInstructResponse> assignmentList, List<Assignment> assignments) {
        Map<Long, Assignment> assignmentMap = new HashMap<>();
        for (Assignment assignment : assignments) {
            assignmentMap.put(assignment.getTaskSubIdx(), assignment);
        }

        for (int i = 1; i <= 10; ++i){
            Long idx = Long.valueOf(i);
            if (assignmentMap.containsKey(idx)) { // 값이 있다면
                assignmentList.add(convertToSingleInstruct(assignmentMap.get(idx)));
            }
            else {
                assignmentList.add(SingleInstructResponse.builder()
                        .similar_instruct(null)
                        .taskSubIdx(0L)
                        .build());
            }
        }
        return assignmentList;
    }


    /**
     *
     * @param assignedTaskId 입력한 입출력 쌍을 알고 싶은 Task의 PK
     */
    public List<AssignIOResponse> getIOPairList(Long userId, Long assignedTaskId, Pageable pageable){
        Task task = taskRepository.findTaskByAssignedId(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        List<AssignIOResponse> ioList = new ArrayList<>();
        List<Assignment> ioPairList = assignRepository.getIOPairList(userId, task.getId(), pageable);
        return getAssignIOList(ioList, ioPairList);
    }

    // Admin인 경우 자신이 작성하지 않은 경우에도 입출력 전체 조회에서 다른 사용자가 작성한 내역을 조회할 수 있음
    public List<AssignIOResponse> adminGetIOPairList(Long assignedTaskId, Pageable pageable){
        User assignedUser = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedTaskId))
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
        Long taskId = taskRepository.findTaskPK(assignedTaskId)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        List<AssignIOResponse> ioList = new ArrayList<>();
        List<Assignment> ioPairList = assignRepository.getIOPairList(assignedUser.getId(), taskId, pageable);

        return getAssignIOList(ioList, ioPairList);
    }
    private List<AssignIOResponse> getAssignIOList(List<AssignIOResponse> ioList, List<Assignment> ioPairList) {
        for (Assignment assignment : ioPairList) {
            ioList.add(convertToAssignIOResponse(assignment));
        }

        int remain = 60 - ioPairList.size();
        for (int i = 0; i < remain; ++i){
            ioList.add(AssignIOResponse.builder()
                    .input(null)
                    .output(null)
                    .build());
        }
        return ioList;
    }









    private AssignIOResponse convertToAssignIOResponse(Assignment assignment){
        return AssignIOResponse.builder()
                .input(assignment.getInput())
                .output(assignment.getOutput())
                .build();
    }
    private AssignResponse convertToAssignResponse(String taskTitle, Assignment assignment){
        return AssignResponse.builder()
                .taskTitle(taskTitle)
                .similarInstruct1(assignment.getSimilarInstruct1())
                .similarInstruct2(assignment.getSimilarInstruct2())
                .taskSubIdx(assignment.getTaskSubIdx())
                .build();
    }
    private SingleInstructResponse convertToSingleInstruct(Assignment assignment){
        return SingleInstructResponse.builder()
                .similar_instruct(assignment.getSimilarInstruct1())
                .taskSubIdx(assignment.getTaskSubIdx())
                .build();
    }
    private TaskResponse convertToDefinition(Assignment assignment, Task task){
        String def1, def2;
        if (assignment.getSimilarInstruct1() == null || assignment.getSimilarInstruct1().equals("")){
            def1 = task.getDefinition1();
        }
        else {
            def1 = assignment.getSimilarInstruct1();
        }
        if (assignment.getSimilarInstruct2() == null || assignment.getSimilarInstruct2().equals("")){
            def2 = task.getDefinition2();
        }
        else {
            def2 = assignment.getSimilarInstruct2();
        }

        return TaskResponse.builder()
                .taskId(task.getAssignedTaskId())
                .taskTitle(task.getTaskStr())
                .definition1(def1)
                .definition2(def2)
                .build();
    }
}
