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
    private Integer sectionNum;
    private String originSection;
    private List<SingleDuplicate> duplicates = new ArrayList<>();


    @Builder
    public DuplicateResponse(Integer sectionNum, String originSection, List<SingleDuplicate> duplicates) {
        this.sectionNum = sectionNum;
        this.originSection = originSection;
        this.duplicates = duplicates;
    }
}
