package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TaskResponse {
    private Long taskId;
    private String instruction; // 지시문
    private String definition_kor;

    private boolean hasNext;
    private boolean hasPrevious;



    @Builder
    public TaskResponse(Long taskId, String instruction, String definition_kor,
                        boolean hasNext, boolean hasPrevious) {
        this.taskId = taskId;
        this.instruction = instruction;
        this.definition_kor = definition_kor;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
}
