package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// 유사지시문 작성 페이지에서 하단에 Task 별로 보이는 입출력쌍에 대한 정보
@Getter
@NoArgsConstructor
@ToString
public class IOResponse {

    private Long taskId; // 속한 Task PK
    private int index; // 1a, 1b..

    private String input1;
    private String input2;

    private String output1;
    private String output2;


    @Builder
    public IOResponse(Long taskId, int index, String input1, String input2,
                      String output1, String output2) {
        this.taskId = taskId;
        this.index = index;
        this.input1 = input1;
        this.input2 = input2;
        this.output1 = output1;
        this.output2 = output2;
    }
}
