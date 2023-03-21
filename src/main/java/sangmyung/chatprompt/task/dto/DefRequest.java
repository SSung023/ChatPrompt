package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class DefRequest {
    private String newDefinition;


    @Builder
    public DefRequest(String newDefinition) {
        this.newDefinition = newDefinition;
    }
}
