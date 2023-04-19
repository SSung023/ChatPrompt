package sangmyung.chatprompt.assignment.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class DuplicateResponse {
    private Long duplicatedIdx; // 중복 대상 subIdx
    private List<SingleDuplicate> partList = new ArrayList<>();


    @Builder
    public DuplicateResponse(Long duplicatedIdx, List<SingleDuplicate> partList) {
        this.duplicatedIdx = duplicatedIdx;
        this.partList = partList;
    }
}
