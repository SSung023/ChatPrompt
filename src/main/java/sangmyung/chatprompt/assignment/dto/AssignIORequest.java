package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class AssignIORequest {
    private String input;
    private String output;


    @Builder
    public AssignIORequest(String input, String output) {
        this.input = input;
        this.output = output;
    }
}
