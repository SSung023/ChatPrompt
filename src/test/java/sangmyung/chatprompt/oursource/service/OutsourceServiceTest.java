package sangmyung.chatprompt.oursource.service;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles({"test"})
@Transactional
@Slf4j
class OutsourceServiceTest {
    @Autowired UserRepository userRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired AssignmentRepository assignmentRepository;
    @Autowired IoPairRepository ioPairRepository;
    @Autowired OutsourceService outsourceService;


    @Test
    @DisplayName("Task PK를 통해 할당된 사용자 확인할 수 있다.")
    public void findAssignedUserByTaskPK(){
        //given
        Long taskPK = taskRepository.findTaskPK(13L)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        //when
        int assignedTaskId = 13;
        User user = userRepository.findAssignedUserByTaskId(assignedTaskId).get();

        //then
        assertThat(user.getId()).isEqualTo(6);
    }

    @Test
    @DisplayName("split 및 모음 횟수 확인 테스트")
    public void splitStringTest(){
        //given
        String str =
                "Sentence: 'a white cat on a desk near a computer monitor'. Count the number of vowels in the given sentence.";

        //when
        String[] split = str.split("'");
        String target = split[1];

        int cnt = 0;
        for(int i = 0; i < target.length(); ++i){
            if (target.charAt(i) == 'a' || target.charAt(i) == 'e' || target.charAt(i) == 'i'
                || target.charAt(i) == 'o' || target.charAt(i) == 'u'){
                cnt++;
            }
        }

        //then
        Assertions.assertThat(target).isEqualTo("a white cat on a desk near a computer monitor");
        Assertions.assertThat(cnt).isEqualTo(16);
    }
    
    @Test
    @DisplayName("모음 제거 테스트")
    public void removeVowelTest(){
        //given
        String str = "AHxSRCLeIapzTcuA";
        String str1 = "ABdarAfALSmLHcTtWiYwPJxtuYsLIiv, fveUiLLHcTtWiYwPJxBtuWVko";
        
        //when
        String newStr1 = str.replaceAll("[aeiouAEIOU]", "");
        String[] strs = str1.split(", ");

        
        //then
        Assertions.assertThat(newStr1).isEqualTo("HxSRCLpzTc");

        for (String s : strs) {
            log.info(s);
        }
    }
    
    @Test
    @DisplayName("문자열 이어붙이기 테스트")
    public void concatTest(){
        //given
        String[] strs = "AWmPPTc, yFmPIN".split(", ");
        String in1 = strs[0];
        String in2 = strs[1];
        String output = "";
        
        //when
        String in = (in1.length() > in2.length() ? in1 : in2).toLowerCase();
        Set<String> sets = new HashSet<>();

        for(int i = 0; i < in.length(); ++i){
            sets.add(String.valueOf(in.charAt(i)));
        }

        List<String> sorted = sets.stream().sorted().toList();
        for (int i = 0; i < sorted.size() - 1; ++i){
            output += sorted.get(i) + ", ";
        }
        output += sorted.get(sorted.size() - 1);

        
        //then
        log.info(output);
    }
    
    @Test
    @DisplayName("F106번 테스트")
    public void F106Test(){
        //given
        String input = "APZtrmpidGzEcWPfTnae, D";

        //when
        String[] splits = input.split(", ");
        
        //then
        Assertions.assertThat(splits[0]).isEqualTo("APZtrmpidGzEcWPfTnae");
        Assertions.assertThat(splits[1]).isEqualTo("D");
    }

    @Test
    @DisplayName("F110번 테스트")
    public void F110Test(){
        //given
        String input = "abCCdDDgGGhi";
        Map<String, String> asciiMap = matchAscii();

        //when
        String output = "";

        Set<String> sets = new HashSet<>();
        for(int i = 0; i < input.length(); ++i){
            String matchedAscii = asciiMap.get(String.valueOf(input.charAt(i)));
            if (!sets.contains(matchedAscii)) {
                output += matchedAscii;
            }
            sets.add(matchedAscii);
        }

        //then
        log.info(output);
//        Assertions.assertThat(output).isEqualTo("AgGzxvBjlCrysmQKf");
    }

    @Test
    @DisplayName("F105번 테스트")
    public void F105Test(){
        //given
        String input = "mvwduljootadondrwbrledodqnmfqtxalvuxnfgft";

        //when
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
        Character c = (char) (idx + 'a');

        //then
        Assertions.assertThat(c).isEqualTo('d');
    }

    @Test
    @DisplayName("F102번 테스트")
    public void F102Test(){
        //given
        String input = "ANRceQkpCPBgiwdyqz";
        String output = "";

        //when
        int maxCode = -1;
        for(int i = 0; i < input.length(); ++i){
            int code = input.charAt(i) - 'A';
            if (maxCode < code){
                maxCode = code;
                output = String.valueOf(input.charAt(i));
            }
        }

        //then
        Assertions.assertThat(output).isEqualTo("z");
    }
    
    @Test
    @DisplayName("E20번 테스트")
    public void E20Test(){
        //given
        String input1 = "cjnxmKkFdEcg";
        String input2 = "iHHnxmKkFeIjOoM";

        String output = "";
        
        //when
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
        
        //then
        Assertions.assertThat(output).isEqualTo("nxmKkF");
    }

    @Test
    @DisplayName("E26 테스트")
    public void E26Test(){
        //given
        String input1 = "cjnxmKkFdEcg";
        String input2 = "iHHnxmKkFeIjOoM";

        //when
        String output = outsourceService.findLCS(input1, input2);
        output = output.toLowerCase();
        List<String> lists = new ArrayList<>();
        for(int i = 0; i < output.length(); ++i){
            lists.add(String.valueOf(output.charAt(i)));
        }
        lists = lists.stream().sorted().toList();

        output = "";
        for (String list : lists) {
            output += list;
        }

        //then
        log.info(output);
    }

    @Test
    @DisplayName("C22번 테스트")
    public void C22Test(){
        //given
        Long taskPK = 46L;

        PageRequest pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> pairs = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = pairs.get(0).getInput();
        String[] split = input.split("'");
        String sentence = split[1];
        String target = split[3];

        int cnt = 0;
        for(int i = 0; i < sentence.length(); ++i){
            if (String.valueOf(sentence.charAt(i)).equals(target)){
                cnt++;
            }
        }

        User user = checkAssignedUser(taskPK);
        Assignment assignment = pairs.get(0);
        assignment.updateOutput(String.valueOf(cnt), user);


        //then
        Assertions.assertThat(cnt).isEqualTo(3);
        Assertions.assertThat(user.getId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("C28번 테스트")
    public void C28Test(){
        //given
        Long taskPK = 52L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
        String[] splits = input.split("'");
        String sentence = splits[1];
        String target = splits[3];

        List<String> lists = Arrays.stream(sentence.split(" ")).toList();
        int cnt = 0;
        for (String list : lists) {
            if (list.equals(target))
                cnt++;
        }

        //then
        log.info(String.valueOf(cnt));
    }

    @Test
    @DisplayName("C29번 테스트")
    public void C29Test(){
        //given
        Long taskPK = 53L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
        String[] splits = input.split("'");

        String sentence1 = splits[1];
        String sentence2 = splits[3];
        String target = splits[5];


        int cnt1 = getFrequency(sentence1, target);
        int cnt2 = getFrequency(sentence2, target);

        //then
        Assertions.assertThat(cnt1).isEqualTo(cnt2);
        log.info(cnt1 + "\n" + cnt2);
    }

    @Test
    @DisplayName("C30번 테스트")
    public void C30Test(){
        //given
        Long taskPK = 54L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
        String[] split = input.split("'");

        String sentence = split[1];
        String target = split[3];
        String replace = split[5];

        String output = sentence.replaceAll(target, replace);
        //then
        log.info("d");
    }

    @Test
    @DisplayName("D9번 테스트")
    public void D9Test(){
        //given
        Long taskPK = 55L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
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

        //then
        Assertions.assertThat(cnt).isEqualTo(2);
    }

    @Test
    @DisplayName("D10번 테스트")
    public void D10Test(){
        //given
        Long taskPK = 56L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
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

        //then
        Assertions.assertThat(cnt).isEqualTo(1);
    }

    @Test
    @DisplayName("D11번 테스트")
    public void D11Test(){
        //given
        Long taskPK = 57L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
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

        //then
        Assertions.assertThat(cnt).isEqualTo(1);
    }

    @Test
    @DisplayName("D30번 테스트")
    public void D30Test(){
        //given
        Long taskPK = 76L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
        String sentence = input.split(": ")[1];
        List<String> lists = List.of(sentence.split(" "));
        String output = "";
        for (int i = lists.size() - 1; i > 0; --i){
            output += lists.get(i) + " ";
        }
        output += lists.get(0);

        //then
        Assertions.assertThat(output).isEqualTo("있다 잔뜩 짐이 주변에 차량 있는 지하주차장에");
    }

    @Test
    @DisplayName("E9번 테스트")
    public void E9Test(){
        //given
        Long taskPK = 77L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
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

        //then
        Assertions.assertThat(output).isEqualTo("아침으로 한 마셨다");
    }

    @Test
    @DisplayName("E10번 테스트")
    public void E10Test(){
        //given
        Long taskPK = 78L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "taskSubIdx"));
        List<Assignment> assignments = assignmentRepository.getIOPairList(2L, taskPK, pageable);

        //when
        String input = assignments.get(0).getInput();
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

        //then
        Assertions.assertThat(output).isEqualTo("에트프리 탄 이들람사 스노보드를 타고 있는 남자를 고하경구 있다");
    }
    
    @Test
    @DisplayName("E28번 테스트")
    public void E28Test(){
        //given
        Long taskPK = 96L;
        PageRequest pageable = PageRequest.of(0, 60, Sort.by(Sort.Direction.ASC, "idx"));
        List<IOPairs> pairs = ioPairRepository.findPairsByTaskId(taskPK, pageable);

        
        //when
        String input = "aaaaaangggannna";
        String output = longestPalindrome(input);

        //then
        Assertions.assertThat(output).isEqualTo("aaaaaa");
    }







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
    private User checkAssignedUser(Long taskPK){
        Long assignedTaskId = taskRepository.findTaskAssignedId(taskPK)
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        User user = userRepository.findAssignedUserByTaskId(Math.toIntExact(assignedTaskId))
                .orElseThrow(() -> new BusinessException(ErrorCode.DATA_ERROR_NOT_FOUND));

        return user;
    }
    private Map<String, String> matchAscii(){
        Map<String, String> asciiMap = new HashMap<>();
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

        return asciiMap;
    }
}