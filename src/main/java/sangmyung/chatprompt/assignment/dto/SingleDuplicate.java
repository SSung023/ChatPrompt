package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class SingleDuplicate {

    private Integer partIdx;
    private String partStr;
    private Boolean isDuplicated;


    @Builder
    public SingleDuplicate(Integer partIdx, String partStr, Boolean isDuplicated) {
        this.partIdx = partIdx;
        this.partStr = partStr;
        this.isDuplicated = isDuplicated;
    }
}
