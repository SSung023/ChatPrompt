package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ValidationIOResponse {
    // 입출력 페이지에서 사용자가 입력하는 창에 해당하는 입출력 정보
    private String input;
    private String output;
    private Boolean isValidated;


    @Builder
    public ValidationIOResponse(String input, String output, Boolean isValidated) {
        this.input = input;
        this.output = output;
        this.isValidated = isValidated;
    }
}
