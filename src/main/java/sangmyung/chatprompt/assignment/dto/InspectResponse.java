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
public class InspectResponse {
    private List<Integer> originDupliIdx = new ArrayList<>();
    private List<DuplicateResponse> duplicatedList = new ArrayList<>();


    @Builder
    public InspectResponse(List<Integer> originDupliIdx, List<DuplicateResponse> duplicatedList) {
        this.originDupliIdx = originDupliIdx;
        this.duplicatedList = duplicatedList;
    }
}
