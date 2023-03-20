package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TaskRequest {
    private int taskId; // 001~120
    private String username; // 작성자 이름


    @Builder
    public TaskRequest(int taskId, String username) {
        this.taskId = taskId;
        this.username = username;
    }
}
