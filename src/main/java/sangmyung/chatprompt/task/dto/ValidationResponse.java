package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ValidationResponse {

    private int isValidated; // 해당 입출력이 검증되었는지 여부
    private int validatedCnt; // 해당 Task에서 검증 완료된 입출력의 수

    @Builder
    public ValidationResponse(int isValidated, int validatedCnt) {
        this.isValidated = isValidated;
        this.validatedCnt = validatedCnt;
    }
}
