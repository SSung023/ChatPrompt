package sangmyung.chatprompt.assignment.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
@NoArgsConstructor
public class SingleDuplicate {

    private Integer targetSubIdx;
    private List<String> partList = new ArrayList<>();
    private List<Integer> partIdx = new ArrayList<>(); // 중복된 part의 번호에 대해 담음


    @Builder
    public SingleDuplicate(Integer targetSubIdx, List<String> partList, List<Integer> partIdx) {
        this.targetSubIdx = targetSubIdx;
        this.partList = partList;
        this.partIdx = partIdx;
    }
}
