package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TaskResponse {

    private String definition_eng;
    private String definition_kor;


    @Builder
    public TaskResponse(String definition_eng, String definition_kor) {
        this.definition_eng = definition_eng;
        this.definition_kor = definition_kor;
    }
}
