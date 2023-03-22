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
    private String definition1; // 지시문 원문
    private String definition2; // 기계 번역문

    private boolean hasNext;
    private boolean hasPrevious;



    @Builder
    public TaskResponse(Long taskId, String definition1, String definition2,
                        boolean hasNext, boolean hasPrevious) {
        this.taskId = taskId;
        this.definition1 = definition1;
        this.definition2 = definition2;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
}
