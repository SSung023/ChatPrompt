package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class AssignRequest {

    private String similarInstruct1;
    private String similarInstruct2;


    @Builder
    public AssignRequest(String similarInstruct1, String similarInstruct2) {
        this.similarInstruct1 = similarInstruct1;
        this.similarInstruct2 = similarInstruct2;
    }
}
