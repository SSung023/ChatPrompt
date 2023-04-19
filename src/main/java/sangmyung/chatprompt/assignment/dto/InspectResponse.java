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

    private String originalInstruct;
    private List<DuplicateResponse> duplicateList = new ArrayList<>();

    @Builder
    public InspectResponse(String originalInstruct, List<DuplicateResponse> duplicateList) {
        this.originalInstruct = originalInstruct;
        this.duplicateList = duplicateList;
    }
}
