package sangmyung.chatprompt.task.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class SingleIOResponse {
    // 특정 IOPair 정보를 담는 DTO

    private IOResponse ioResponse;
    private boolean hasNext;
    private boolean hasPrevious;


    @Builder
    public SingleIOResponse(IOResponse ioResponse, boolean hasNext, boolean hasPrevious) {
        this.ioResponse = ioResponse;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
}
