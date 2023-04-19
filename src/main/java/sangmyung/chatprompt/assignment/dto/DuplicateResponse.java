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
    private Integer originSection;
    private List<SingleDuplicate> partList = new ArrayList<>();


    @Builder
    public DuplicateResponse(Integer originSection, List<SingleDuplicate> partList) {
        this.originSection = originSection;
        this.partList = partList;
    }
}
