package sangmyung.chatprompt.user.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sangmyung.chatprompt.assignment.domain.Assignment;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Assignment> assignList = new ArrayList<>();

    private String identifier; // 구분자
    private String name; // 이름

    private int taskStartIdx; // 사용자가 할당받은 Task 시작 번호
    private int taskEndIdx; // 사용자가 할당받은 Task 끝 번호

    private Long lastTaskNum;


    @Builder
    public User(String identifier, String name, Long lastTaskNum, int taskStartIdx, int taskEndIdx) {
        this.identifier = identifier;
        this.name = name;
        this.lastTaskNum = lastTaskNum;
        this.taskStartIdx = taskStartIdx;
        this.taskEndIdx = taskEndIdx;
    }

    //== 비지니스 코드 ==//
    // 마지막으로 수정한 Task의 번호를 갱신
    public void updateLastTaskNum(Long lastTaskNum){
        this.lastTaskNum = lastTaskNum;
    }
}
