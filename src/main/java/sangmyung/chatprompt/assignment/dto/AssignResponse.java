package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class AssignResponse {
    private String taskTitle;
    private String similarInstruct1;
    private String similarInstruct2;
    private Long taskSubIdx;

//    private String input;
//    private String output;


    @Builder
    public AssignResponse(String taskTitle, String similarInstruct1, String similarInstruct2, Long taskSubIdx) {
        this.taskTitle = taskTitle;
        this.similarInstruct1 = similarInstruct1;
        this.similarInstruct2 = similarInstruct2;
        this.taskSubIdx = taskSubIdx;
//        this.input = input;
//        this.output = output;
    }
}
