package sangmyung.chatprompt.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserResponse {

    private Long lastModifiedTaskNum;
    private int taskStartIdx;
    private int taskEndIdx;
    private int ioStartIdx;
    private int ioEndIdx;


    @Builder

    public UserResponse(Long lastModifiedTaskNum, int taskStartIdx, int taskEndIdx, int ioStartIdx, int ioEndIdx) {
        this.lastModifiedTaskNum = lastModifiedTaskNum;
        this.taskStartIdx = taskStartIdx;
        this.taskEndIdx = taskEndIdx;
        this.ioStartIdx = ioStartIdx;
        this.ioEndIdx = ioEndIdx;
    }
}
