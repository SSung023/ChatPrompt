package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class AssignIOResponse {
    // 입출력 페이지에서 사용자가 입력하는 창에 해당하는 입출력 정보
    private String input;
    private String output;


    @Builder
    public AssignIOResponse(String input, String output) {
        this.input = input;
        this.output = output;
    }
}
