package sangmyung.chatprompt.oursource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.assignment.domain.Assignment;
import sangmyung.chatprompt.assignment.repository.AssignmentRepository;
import sangmyung.chatprompt.task.domain.IOPairs;
import sangmyung.chatprompt.task.repository.IoPairRepository;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.repository.UserRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OutsourceService {
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final IoPairRepository ioPairRepository;

    private Map<String, String> asciiMap = new HashMap<>();

    private List<Integer> type0List = new ArrayList<>();
    private List<Integer> type1List = new ArrayList<>();


    /**
     * 0번 타입 입출력을 처리하는 함수
     */
    @Transactional
    public void extractType0(Pageable pageable){
        addType0TaskPK(); // Type0인 모든 Task의 PK를 ArrayList에 저장

        User user = userRepository.findById(2L).get();

        for (Integer taskPK : type0List) {
            List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(Long.valueOf(taskPK), pageable);
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

        User user = userRepository.findById(2L).get();

        for (Integer taskPK : type1List) {
            List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(Long.valueOf(taskPK), pageable);
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

    // Type1의 단순 치환을 제외한
    @Transactional
    public void extractType1(Pageable pageable){
        addType1TaskPK();
        matchAscii();

        User user = userRepository.findById(2L).get();

        extract_task48(pageable, user, Long.valueOf(type1List.get(0)));
        extract_task87(pageable, user, Long.valueOf(type1List.get(1)));
        extract_task99(pageable, user, Long.valueOf(type1List.get(2)));
        extract_task112(pageable, user, Long.valueOf(type1List.get(3)));
        extract_task115(pageable, user, Long.valueOf(type1List.get(4)));

        log.info("type 1 단순 치환 제외 테스크 변환 완료");
    }

    private void extract_task48(Pageable pageable, User user, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);

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
    private void extract_task87(Pageable pageable, User user, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);

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
    private void extract_task99(Pageable pageable, User user, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);

        for (IOPairs pair : pairs) {
            String input = pair.getInput1().replace("target", "대상");
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
    private void extract_task112(Pageable pageable, User user, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);

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
    private void extract_task115(Pageable pageable, User user, Long taskPK) {
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);

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
            return "숫자 승리";
        }
        else if (inputEng.equals("Alphabets Win")) {
            return "문자 승리";
        }
        else if (inputEng.equals("Numbers and Alphabets are Tied")) {
            return "숫자와 문자 비김";
        }
        throw new BusinessException(ErrorCode.INVALID_PARAMETER);
    }

}