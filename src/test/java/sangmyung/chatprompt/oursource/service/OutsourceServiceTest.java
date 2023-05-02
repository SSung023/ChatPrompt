package sangmyung.chatprompt.oursource.service;


import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
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
        String input = "AgGgzxvBjlCrysBmzQKfz";

        //when
        String output = "";

        Set<Character> sets = new HashSet<>();
        for(int i = 0; i < input.length(); ++i){
            if (!sets.contains(input.charAt(i))) {
                output += input.charAt(i);
            }
            sets.add(input.charAt(i));
        }

        //then
        Assertions.assertThat(output).isEqualTo("AgGzxvBjlCrysmQKf");
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

}