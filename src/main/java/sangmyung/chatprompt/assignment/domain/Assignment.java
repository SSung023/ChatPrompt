package sangmyung.chatprompt.assignment.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sangmyung.chatprompt.user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long taskId; // Task PK

    private String similarInstruct1; // 유사지시문1
    private String similarInstruct2; // 유사지시문2

    private String input; // 입력
    private String output; // 출력

    private Long ioIndex; // input&output 인덱스 ex) 1a, 1b에서의 1


    @Builder
    public Assignment(Long taskId, String similarInstruct1, String similarInstruct2,
                      String input, String output, Long ioIndex) {
        this.taskId = taskId;
        this.similarInstruct1 = similarInstruct1;
        this.similarInstruct2 = similarInstruct2;
        this.input = input;
        this.output = output;
        this.ioIndex = ioIndex;
    }


    //=== 연관관계 편의메서드 ===//
    public void addUser(User user){
        this.user = user;
        user.getAssignList().add(this);
    }

    //=== 비지니스 코드 ===//
    // 유사 지시문1 & 2 갱신
    public void updateSimilarInstruct(String similarInstruct1, String similarInstruct2){
        this.similarInstruct1 = similarInstruct1;
        this.similarInstruct2 = similarInstruct2;
    }

    // 입력 & 출력 갱신
    public void updateIO(String input, String output){
        this.input = input;
        this.output = output;
    }
}
