package sangmyung.chatprompt.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String identifier; // 구분자
    private String name; // 이름

    private Long lastTaskNum;


    @Builder
    public User(String identifier, String name, Long lastTaskNum) {
        this.identifier = identifier;
        this.name = name;
        this.lastTaskNum = lastTaskNum;
    }

    //== 비지니스 코드 ==//
    // 마지막으로 수정한 Task의 번호를 갱신
    public void updateLastTaskNum(Long lastTaskNum){
        this.lastTaskNum = lastTaskNum;
    }
}
