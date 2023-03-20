package sangmyung.chatprompt.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class UserRequest {
    private String username;


    @Builder
    public UserRequest(String username) {
        this.username = username;
    }
}
