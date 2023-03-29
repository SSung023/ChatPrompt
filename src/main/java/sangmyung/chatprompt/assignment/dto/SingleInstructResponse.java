package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class SingleInstructResponse {
    private String similar_instruct;
    private Long taskSubIdx;


    @Builder
    public SingleInstructResponse(String similar_instruct, Long taskSubIdx) {
        this.similar_instruct = similar_instruct;
        this.taskSubIdx = taskSubIdx;
    }
}
