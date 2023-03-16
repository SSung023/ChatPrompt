package sangmyung.chatprompt.Home.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Home {
    @Id @GeneratedValue
    private Long id;

    private String test;

    @Builder
    public Home(String test) {
        this.test = test;
    }
}
