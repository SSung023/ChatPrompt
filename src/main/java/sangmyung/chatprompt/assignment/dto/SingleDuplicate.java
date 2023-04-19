package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SingleDuplicate {

    private Integer targetSubIdx;
    private String previous;
    private String duplicated;
    private String next;


    @Builder
    public SingleDuplicate(Integer targetSubIdx, String previous, String duplicated, String next) {
        this.targetSubIdx = targetSubIdx;
        this.previous = previous;
        this.duplicated = duplicated;
        this.next = next;
    }
}
