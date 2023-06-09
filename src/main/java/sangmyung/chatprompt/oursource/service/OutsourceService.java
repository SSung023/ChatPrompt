package sangmyung.chatprompt.oursource.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import sangmyung.chatprompt.task.repository.IoPairRepository;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OutsourceService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final AssignmentRepository assignmentRepository;
    private final IoPairRepository ioPairRepository;

    private Map<String, String> asciiMap = new HashMap<>();

    private List<Integer> type0List = new ArrayList<>();
    private List<Integer> type1List = new ArrayList<>();


    /**
     * 등록된 입출력 간에 중복된 입출력이 있는지 여부 확인
     */
    public void checkDuplicate(){
        List<Assignment> assignmentList = assignmentRepository.findAllAssignment();
        Set<ioPairSet> assignSet = new HashSet<>();
        Map<ioPairSet, TaskInfo> assignMap = new HashMap<>();
        int cnt = 0;

        for (Assignment assignment : assignmentList) {
            String input = assignment.getInput();
            String output = assignment.getOutput();
            Long assignedTaskId = taskRepository.findTaskAssignedId(assignment.getTaskId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
            int ioPairsIdx = assignment.getIoPairsIdx();


            ioPairSet pairSet = new ioPairSet(input, output, assignedTaskId, ioPairsIdx);
            TaskInfo taskInfo = new TaskInfo(assignedTaskId, ioPairsIdx);

            if (assignMap.containsKey(pairSet)){
                TaskInfo info = assignMap.get(pairSet);
                log.info("input/output 중복 발생"
                        + "\ntaskId: " + info.getAssignedTaskId() + " ioPairIdx: " + info.getIoPairsIdx()
                        + "\ntaskId: " + assignedTaskId + " ioPairIdx: " + ioPairsIdx
                        + "\t총 중복 개수: " + ++cnt);
            }
            else {
                assignMap.put(pairSet, taskInfo);
            }
//            if (assignSet.contains(pairSet)){
//                log.info("input/output 중복 발생"
//                        + "\ntaskId: " + assignedTaskId + " ioPairIdx: " + ioPairsIdx
//                        + "\t총 중복 개수: " + ++cnt);
//            }
//            else {
//                // input, output이 포함되어있지 않으면 추가
//                assignSet.add(pairSet);
//            }
        }
    }

    /**
     * input만 중복 확인
     */
    public void checkDuplicateOnlyInput(){
        List<Assignment> assignmentList = assignmentRepository.findAllAssignment();
        Set<ioPairSet> assignSet = new HashSet<>();
        int cnt = 0;

        for (Assignment assignment : assignmentList) {
            String input = assignment.getInput();
            Long assignedTaskId = taskRepository.findTaskAssignedId(assignment.getTaskId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
            int ioPairsIdx = assignment.getIoPairsIdx();


            ioPairSet pairSet = new ioPairSet(input, "", assignedTaskId, ioPairsIdx);

            if (assignSet.contains(pairSet)){
                log.info("input 중복 발생"
                        + "\ntaskId: " + assignedTaskId + " ioPairIdx: " + ioPairsIdx
                        + "\t총 중복 개수: " + ++cnt);
            }
            else {
                // input, output이 포함되어있지 않으면 추가
                assignSet.add(pairSet);
            }
        }
    }
    @Getter
    class ioPairSet{
        String input;
        String output;
        Long assignedTaskId;
        int ioPairsIdx;

        public ioPairSet(String input, String output, Long assignedTaskId, int ioPairsIdx) {
            this.input = input;
            this.output = output;
            this.assignedTaskId = assignedTaskId;
            this.ioPairsIdx = ioPairsIdx;
        }

        @Override
        public boolean equals(Object obj) {
            ioPairSet target = (ioPairSet) obj;
            return this.input.equals(target.getInput()) && this.output.equals(target.getOutput());
        }

        @Override
        public int hashCode() {
            return this.input.hashCode() + this.output.hashCode();
        }
    }
    @Getter
    class TaskInfo {
        Long assignedTaskId;
        int ioPairsIdx;

        public TaskInfo(Long assignedTaskId, int ioPairsIdx) {
            this.assignedTaskId = assignedTaskId;
            this.ioPairsIdx = ioPairsIdx;
        }
    }

    /**
     * 0번 타입 입출력을 처리하는 함수
     */
    @Transactional
    public void extractType0(Pageable pageable){
        addType0TaskPK(); // Type0인 모든 Task의 PK를 ArrayList에 저장

        for (Integer taskPK : type0List) {
            List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(Long.valueOf(taskPK), pageable);
            User user = checkAssignedUser(Long.valueOf(taskPK));

            for (IOPairs pair : pairs) {
                Assignment assignment = Assignment.builder()
                        .input(pair.getInput1())
                        .output(pair.getOutput1())
                        .taskId(Long.valueOf(taskPK))
                        .ioPairsIdx(pair.getIdx())
                        .build();
                Assignment savedAssignment = assignmentRepository.save(assignment);
                savedAssignment.addUser(user);
            }
            log.info(taskPK + "번 입출력 등록 완료");
        }
        log.info("Type0에 해당하는 입출력 등록 완료");
    }

    @Transactional
    public void extractType1_SimpleConvert(Pageable pageable){
        addType1SimpleConvert();
        matchAscii();

        for (Integer taskPK : type1List) {
            List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(Long.valueOf(taskPK), pageable);
            User user = checkAssignedUser(Long.valueOf(taskPK));

            for (IOPairs pair : pairs) {
                String input = type1_convertToKor(pair.getInput1());
                String output = type1_convertToKor(pair.getOutput1());

                Assignment assignment = Assignment.builder()
                        .input(input)
                        .output(output)
                        .taskId(Long.valueOf(taskPK))
                        .ioPairsIdx(pair.getIdx())
                        .build();
                Assignment savedAssignment = assignmentRepository.save(assignment);
                savedAssignment.addUser(user);
            }
        }
        log.info("type 1 단순 변환 등록 완료");
    }

    // Type1의 단순 치환 제외
    @Transactional
    public void extractType1(Pageable pageable){
        addType1TaskPK();
        matchAscii();

        extract_task48(pageable, Long.valueOf(type1List.get(0)));
        extract_task87(pageable, Long.valueOf(type1List.get(1)));
        extract_task99(pageable, Long.valueOf(type1List.get(2)));
        extract_task112(pageable, Long.valueOf(type1List.get(3)));
        extract_task115(pageable, Long.valueOf(type1List.get(4)));

        log.info("type 1 단순 치환 제외 테스크 변환 완료");
    }

    private void extract_task48(Pageable pageable, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1().replace("first", "첫 번째");
            input = input.replace("second", "두 번째");
            String output = pair.getOutput1().replace("first", "첫 번째");
            output = output.replace("second", "두 번째");

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }
    private void extract_task87(Pageable pageable, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = type1_convertToKor(pair.getInput1());
            String output = type1_task79_convert(pair.getOutput1());

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }
    private void extract_task99(Pageable pageable, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1().replace("target", "목표 값");
            String output = pair.getOutput1();

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }
    private void extract_task112(Pageable pageable, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1().replace("input_format", "입력 형식");
            String output = pair.getOutput1();

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }
    private void extract_task115(Pageable pageable, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = convertToKorNum(pair.getInput1());
            String output = pair.getOutput1();

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }



    @Transactional
    public void extract_C22(){
        Long taskPK = 46L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();

            String[] splits = input.split("'");
            String sentence = splits[1];
            String target = splits[3];

            int cnt = 0;
            for(int i = 0; i < sentence.length(); ++i){
                if (String.valueOf(sentence.charAt(i)).equals(target)){
                    cnt++;
                }
            }

            assignment.updateOutput(String.valueOf(cnt), user);
        }
    }
    @Transactional
    public void extract_C28(){
        Long taskPK = 52L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] splits = input.split("'");
            String sentence = splits[1];
            String target = splits[3];

            List<String> lists = Arrays.stream(sentence.split(" ")).toList();
            int cnt = 0;
            for (String list : lists) {
                if (list.equals(target))
                    cnt++;
            }

            assignment.updateOutput(String.valueOf(cnt), user);
        }
    }

    @Transactional
    public void extract_C29(){
        Long taskPK = 53L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] splits = input.split("'");

            String sentence1 = splits[1];
            String sentence2 = splits[3];
            String target = splits[5];


            int cnt1 = getFrequency(sentence1, target);
            int cnt2 = getFrequency(sentence2, target);

            String output = cnt1 == cnt2 ? "예" : "아니오";
            assignment.updateOutput(output, user);
        }
    }

    @Transactional
    public void extract_C30(){
        Long taskPK = 54L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] split = input.split("'");

            String sentence = split[1];
            String target = split[3];
            String replace = split[5];

            String output = sentence.replaceAll(target, replace);
            assignment.updateOutput(output, user);
        }
    }

    @Transactional
    public void extract_D9(){
        Long taskPK = 55L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] split = input.split("'");

            String sentence = split[1];
            String target = split[3];

            List<String> lists = List.of(sentence.split(" "));
            int cnt = 0;
            for (String str : lists) {
                if (str.contains(target)){
                    cnt++;
                }
            }
            assignment.updateOutput(String.valueOf(cnt), user);
        }
    }
    @Transactional
    public void extract_D10(){
        Long taskPK = 56L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] split = input.split("'");

            String sentence = split[1];
            String target = split[3];

            List<String> lists = List.of(sentence.split(" "));
            int cnt = 0;
            for (String str : lists) {
                if (str.startsWith(target)){
                    cnt++;
                }
            }
            assignment.updateOutput(String.valueOf(cnt), user);
        }
    }
    @Transactional
    public void extract_D11(){
        Long taskPK = 57L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] split = input.split("'");

            String sentence = split[1];
            String target = split[3];

            List<String> lists = List.of(sentence.split(" "));
            int cnt = 0;
            for (String str : lists) {
                if (str.endsWith(target)){
                    cnt++;
                }
            }
            assignment.updateOutput(String.valueOf(cnt), user);
        }
    }

    @Transactional
    public void extract_D30(){
        Long taskPK = 76L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String sentence = input.split(": ")[1];
            List<String> lists = List.of(sentence.split(" "));
            String output = "";
            for (int i = lists.size() - 1; i > 0; --i){
                output += lists.get(i) + " ";
            }
            output += lists.get(0);

            assignment.updateOutput(output, user);
        }
    }

    @Transactional
    public void extract_D46(Pageable pageable){
        Long taskPK = 62L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String engInput = pair.getInput1();
            String output = pair.getOutput1();

            String[] split = engInput.split("'");
            String input = "집합1: " + split[1] + ", 집합2: " + split[3] + ". 집합1과 집합2의 교집합의 원소 개수는 몇 개입니까?";

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_D47(Pageable pageable){
        Long taskPK = 63L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String engInput = pair.getInput1();
            String output = pair.getOutput1();

            String[] split = engInput.split("'");
            String input = "집합1: " + split[1] + ", 집합2: " + split[3] + ". 집합1과 집합2의 합집합의 원소 개수는 몇 개입니까?";

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_D48(Pageable pageable){
        Long taskPK = 64L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String engInput = pair.getInput1();
            String output = pair.getOutput2();

            String[] split = engInput.split("'");
            String input = "집합1: " + split[1] + ", 집합2: " + split[3] + ". 집합1과 집합2의 교집합에 원소 ‘" + split[5] + "’가 있나요? ";

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_E9(){
        Long taskPK = 77L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] split = input.split("'");

            String sentence = split[1];
            int targetLen = Integer.parseInt(split[3]);

            List<String> lists = List.of(sentence.split(" "));
            String output = "";
            for (String str : lists) {
                if (str.length() == targetLen)
                    continue;

                output += str + " ";
            }
            output = output.substring(0, output.length()-1);
            assignment.updateOutput(output, user);
        }
    }

    @Transactional
    public void extract_E10(){
        Long taskPK = 78L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (Assignment assignment : assignments) {
            String input = assignment.getInput();
            String[] split = input.split("'");
            String sentence = split[1];
            int targetLen = Integer.parseInt(split[3]);

            List<String> lists = List.of(sentence.split(" "));
            String output = "";
            for (String str : lists) {
                if (str.length() == targetLen){
                    String reverse = "";
                    for(int i = str.length() - 1; i >= 0; --i){
                        reverse += str.charAt(i);
                    }
                    output += reverse + " ";
                    continue;
                }

                output += str + " ";
            }
            output = output.substring(0, output.length()-1);
            assignment.updateOutput(output, user);
        }
    }

    @Transactional
    public void extract_E23(Pageable pageable){
        Long taskPK = 91L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1();
            String output = pair.getOutput1();

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_C27(Pageable pageable){
        Long taskPK = 51L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);


        for (IOPairs pair : pairs) {
            String input = pair.getInput1();
            String target = input.split("'")[1];

            int cnt = 0;
            String inputKor = "문장: '" + target;

            // 검사 대상 문자열에서 모음의 등장 횟수 확인
            if (input.contains("vowels")){
                for(int i = 0; i < target.length(); ++i){
                    if (target.charAt(i) == 'a' || target.charAt(i) == 'e' || target.charAt(i) == 'i'
                            || target.charAt(i) == 'o' || target.charAt(i) == 'u'){
                        cnt++;
                    }
                }
                inputKor += "'. 주어진 문장에서 모음의 수를 세십시오.";
            }

            // 검사 대상 문자열에서 자음의 등장 횟수 확인
            if (input.contains("consonants")){
                for(int i = 0; i < target.length(); ++i){
                    if (target.charAt(i) == 'a' || target.charAt(i) == 'e' || target.charAt(i) == 'i'
                            || target.charAt(i) == 'o' || target.charAt(i) == 'u' || target.charAt(i) == ' '){
                        continue;
                    }
                    cnt++;
                }
                inputKor += "'. 주어진 문장에서 자음의 수를 세십시오.";
            }

            Assignment assignment = Assignment.builder()
                    .input(inputKor)
                    .output(String.valueOf(cnt))
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_D20(Pageable pageable){
        Long taskPK = 66L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1();
            String output = pair.getOutput1().replaceAll("[aeiouAEIOU]", "");

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_E20(Pageable pageable){
        Long taskPK = 88L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String[] splits = pair.getInput1().split(", ");
            String input1 = splits[0];
            String input2 = splits[1];
            String output = "";

            // find LCS
            output = findLCS(input1, input2);

            Assignment assignment = Assignment.builder()
                    .input(pair.getInput1())
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_E26(Pageable pageable){
        Long taskPK = 94L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        matchAscii();

        for (IOPairs pair : pairs) {
            // input을 한글로 만들고, 두 단어로 나누기
            String inputKor = type1_convertToKor(pair.getInput1());
            String[] splits = inputKor.split(", ");
            String input1 = splits[0];
            String input2 = splits[1];

            // find LCS
            String lcs = findLCS(input1, input2);
            String sortedLcs = "";

            // LCS를 한글 순으로 정렬
            List<String> lists = new ArrayList<>();
            for(int i = 0; i < lcs.length(); ++i){
                lists.add(String.valueOf(lcs.charAt(i)));
            }
            lists = lists.stream().sorted().toList();

            for (String str : lists) {
                sortedLcs += str;
            }

            // 기존 input에서 LCS를 찾고
            input1 = input1.replace(lcs, sortedLcs);
            input2 = input2.replace(lcs, sortedLcs);
            String output = input1 + ", " + input2;

            // 새로운 Assignment 등록
            Assignment assignment = Assignment.builder()
                    .input(inputKor)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_E27(Pageable pageable){
        Long taskPK = 95L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        matchAscii();

        for (IOPairs pair : pairs) {
            String[] str = pair.getInput1().split(", ");
            String input = matchToKor(pair.getInput1());
            String in1 = matchToKor(str[0]);
            String in2 = matchToKor(str[1]);
            String output = "";

            String in = in1.length() > in2.length() ? in1 : in2;
            Set<String> sets = new HashSet<>();

            for(int i = 0; i < in.length(); ++i){
                sets.add(String.valueOf(in.charAt(i)));
            }

            List<String> sorted = sets.stream().sorted().toList();
            for (int i = 0; i < sorted.size() - 1; ++i){
                output += sorted.get(i) + ", ";
            }
            output += sorted.get(sorted.size() - 1);

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_E28(Pageable pageable){
        Long taskPK = 96L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1();
            String output = longestPalindrome(input);

            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_F102(Pageable pageable){
        Long taskPK = 102L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1();
            String output = "";

            int maxCode = -1;
            for(int i = 0; i < input.length(); ++i){
                int code = input.charAt(i) - 'A';
                if (maxCode < code){
                    maxCode = code;
                    output = String.valueOf(input.charAt(i));
                }
            }
            Assignment assignment = Assignment.builder()
                    .input(input)
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_F105(Pageable pageable){
        Long taskPK = 105L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1();
            int[] lists = new int[26];

            for(int i = 0; i < input.length(); ++i){
                int i1 = input.charAt(i) - 'a';
                int cnt = lists[i1];
                lists[i1] = cnt + 1;
            }

            int result = -1;
            int idx = -1;
            for (int i = 0; i < 26; ++i){
                if (result < lists[i]){
                    result = lists[i];
                    idx = i;
                }
            }
            Character output = (char) (idx + 'a');

            Assignment assignment = Assignment.builder()
                    .input(pair.getInput1())
                    .output(String.valueOf(output))
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_F106(Pageable pageable){
        Long taskPK = 106L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        for (IOPairs pair : pairs) {
            String[] strings = pair.getInput1().split(", ");
            String input = strings[0];
            String compare = strings[1];

            String output = input.contains(compare) ? "1" : "0";

            Assignment assignment = Assignment.builder()
                    .input(pair.getInput1())
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }

    @Transactional
    public void extract_F110(Pageable pageable){
        Long taskPK = 110L;
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);
        User user = checkAssignedUser(taskPK);

        matchAscii();

        for (IOPairs pair : pairs) {
            String input = pair.getInput1();
            String output = "";

            Set<String> sets = new HashSet<>();
            for(int i = 0; i < input.length(); ++i){
                String matchedAscii = asciiMap.get(String.valueOf(input.charAt(i)));
                if (!sets.contains(matchedAscii)) {
                    output += matchedAscii;
                }
                sets.add(matchedAscii);
            }

            Assignment assignment = Assignment.builder()
                    .input(matchToKor(input))
                    .output(output)
                    .taskId(taskPK)
                    .ioPairsIdx(pair.getIdx())
                    .build();
            Assignment savedAssignment = assignmentRepository.save(assignment);
            savedAssignment.addUser(user);
        }
    }



    @Transactional
    public void convertEngToKor(){
        matchAscii();

        List<Long> assignedIds = new ArrayList<>();
        assignedIds.add(80L);
        assignedIds.add(86L);
        assignedIds.add(87L);
        assignedIds.add(88L);
        assignedIds.add(102L);
        assignedIds.add(105L);
        assignedIds.add(106L);

        for (Long assignedId : assignedIds) {
            Long taskPK = taskRepository.findTaskPK(assignedId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));
            User user = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedId))
                    .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

            PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "ioPairsIdx"));
            List<Assignment> lists = assignmentRepository.getIOPairList(user.getId(), taskPK, pageable);
            for (Assignment assignment : lists) {
                String input = matchToKor(assignment.getInput());
                String output = matchToKor(assignment.getOutput());

                assignment.updateIO(input, output);
            }
        }
    }





    // Task의 assignedId를 확인하고 할당받은 사용자의 PK를 반환
    private User checkAssignedUser(Long taskPK){
        Long assignedTaskId = taskRepository.findTaskAssignedId(taskPK)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        User user = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedTaskId))
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        return user;
    }

    private String matchToKor(String originInput){
        String result = "";
        for (int i = 0; i < originInput.length(); ++i){
            if (asciiMap.containsKey(String.valueOf(originInput.charAt(i)))){
                String matchedAscii = asciiMap.get(String.valueOf(originInput.charAt(i)));
                result += matchedAscii;
            }
            else
                result += originInput.charAt(i);

        }
        return result;
    }



    private void addType0TaskPK(){
        type0List.addAll(Arrays.asList
                        (37, 38, 39, 40, 41, 42, 47, 49, 50, 58, 59, 60
                         ,67, 68, 69, 70, 71, 72, 73, 74, 75 ,97, 98, 100
                         ,101 ,103, 104, 107, 108, 109, 111, 113, 114, 116
                         ,117, 118)
                        );
    }
    private void addType1SimpleConvert(){
        type1List.clear();
        type1List.addAll(Arrays.asList(
                32, 33, 34, 35, 36, 43, 44, 45,
                61, 65, 79, 80, 81, 82, 83, 84, 85, 86, 89,
                90, 91, 92, 93, 119, 120
        ));
    }
    private void addType1TaskPK(){
        type1List.clear();
        type1List.addAll(Arrays.asList(
            48, 87, 99, 112, 115
        ));
    }

    private void matchAscii(){
        asciiMap.clear();

        asciiMap.put("A","가");
        asciiMap.put("B","거");
        asciiMap.put("C","고");
        asciiMap.put("D","구");
        asciiMap.put("E","나");
        asciiMap.put("F","너");
        asciiMap.put("G","노");
        asciiMap.put("H","누");
        asciiMap.put("I","다");
        asciiMap.put("J","더");
        asciiMap.put("K","도");
        asciiMap.put("L","두");
        asciiMap.put("M","라");
        asciiMap.put("N","러");
        asciiMap.put("O","로");
        asciiMap.put("P","루");
        asciiMap.put("Q","마");
        asciiMap.put("R","머");
        asciiMap.put("S","모");
        asciiMap.put("T","바");
        asciiMap.put("U","버");
        asciiMap.put("V","보");
        asciiMap.put("W","부");
        asciiMap.put("X","사");
        asciiMap.put("Y","서");
        asciiMap.put("Z","소");

        asciiMap.put("a","수");
        asciiMap.put("b","아");
        asciiMap.put("c","어");
        asciiMap.put("d","오");
        asciiMap.put("e","우");
        asciiMap.put("f","자");
        asciiMap.put("g","저");
        asciiMap.put("h","조");
        asciiMap.put("i","주");
        asciiMap.put("j","차");
        asciiMap.put("k","처");
        asciiMap.put("l","초");
        asciiMap.put("m","추");
        asciiMap.put("n","카");
        asciiMap.put("o","커");
        asciiMap.put("p","코");
        asciiMap.put("q","타");
        asciiMap.put("r","터");
        asciiMap.put("s","토");
        asciiMap.put("t","투");
        asciiMap.put("u","파");
        asciiMap.put("v","퍼");
        asciiMap.put("w","포");
        asciiMap.put("x","하");
        asciiMap.put("y","허");
        asciiMap.put("z","호");
    }
    private Map<String, String> matchNum(){
        Map<String, String> numMap = new HashMap<>();
        numMap.put("zero","영");
        numMap.put("one", "일");
        numMap.put("two","이");
        numMap.put("three","삼");
        numMap.put("four","사");
        numMap.put("five","오");
        numMap.put("six","육");
        numMap.put("seven","칠");
        numMap.put("eight","팔");
        numMap.put("nine","구");
        return numMap;
    }
    private String convertToKorNum(String inputEng){
        Map<String, String> map = matchNum();

        for (Map.Entry<String, String> entry : map.entrySet()) {
            inputEng = inputEng.replace(entry.getKey(), entry.getValue());
        }
        return inputEng;
    }

    private String type1_convertToKor(String inputEng){
        matchAscii();

        String inputKor = "";
        int len = inputEng.length();
        for(int i = 0; i < len; ++i){
            // 영문자인 경우
            if ((65 <= inputEng.charAt(i) && inputEng.charAt(i) <= 90) || (97 <= inputEng.charAt(i) && inputEng.charAt(i) <= 122)) {
                inputKor += asciiMap.get(String.valueOf(inputEng.charAt(i)));
            }
            else {
                inputKor += inputEng.charAt(i);
            }
        }
        return inputKor;
    }

    private String type1_task79_convert(String inputEng){
        if (inputEng.equals("Numbers Win")) {
            return "숫자 승";
        }
        else if (inputEng.equals("Alphabets Win")) {
            return "문자 승";
        }
        else if (inputEng.equals("Numbers and Alphabets are Tied")) {
            return "무승부";
        }
        throw new BusinessException(ErrorCode.INVALID_PARAMETER);
    }


    public String findLCS(String input1, String input2){
        String output = "";

        int n = input1.length();
        int m = input2.length();

        int[][] dp = new int[n+1][m+1];
        int maxLength = 0;
        int endIndex = -1;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (input1.charAt(i-1) == input2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                    if (dp[i][j] > maxLength) {
                        maxLength = dp[i][j];
                        endIndex = i - 1;
                    }
                }
            }
        }
        if (maxLength == 0) {
            output = "";
        } else {
            output = input1.substring(endIndex - maxLength + 1, endIndex + 1);
        }
        return output;
    }

    // sentence 내에 target이 몇 번 등장하는지 확인
    private int getFrequency(String sentence, String target){
        List<String> lists = List.of(sentence.split(" "));
        int cnt = 0;
        for (String str : lists) {
            if (str.equals(target)){
                cnt++;
            }
        }
        return cnt;
    }

    // 회문 관련 코드
    private String longestPalindrome(String s) {
        if (s == null || s.length() < 1) return "";
        int start = 0, end = 0;
        for (int i = 0; i < s.length(); i++) {
            int len1 = expandAroundCenter(s, i, i);
            int len2 = expandAroundCenter(s, i, i + 1);
            int len = Math.max(len1, len2);
            if (len > end - start) {
                start = i - (len - 1) / 2;
                end = i + len / 2;
            }
        }
        String result = s.substring(start, end + 1);
        if (result.length() == 1){
            return String.valueOf(s.charAt(0));
        }
        return result;
    }
    private int expandAroundCenter(String s, int left, int right) {
        int L = left, R = right;
        while (L >= 0 && R < s.length() && s.charAt(L) == s.charAt(R)) {
            L--;
            R++;
        }
        return R - L - 1;
    }
}