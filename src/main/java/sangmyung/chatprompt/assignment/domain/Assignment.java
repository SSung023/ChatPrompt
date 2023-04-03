package sangmyung.chatprompt.assignment.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sangmyung.chatprompt.assignment.dto.AssignRequest;
import sangmyung.chatprompt.task.domain.IOPairs;
import sangmyung.chatprompt.user.domain.User;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor()
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "io_pairs_id")
    private IOPairs ioPairs;

    private Long taskId; // Task PK

    @Column(columnDefinition = "TEXT")
    private String similarInstruct1; // 유사지시문1

    @Column(columnDefinition = "TEXT")
    private String similarInstruct2; // 유사지시문2

    @Column(columnDefinition = "TEXT")
    private String input; // 입력
    @Column(columnDefinition = "TEXT")
    private String output; // 출력

    private Long taskSubIdx; // Task마다 유사지시문의 인덱스 (1~10의 범위)


    @Builder
    public Assignment(Long taskId, String similarInstruct1, String similarInstruct2,
                      String input, String output) {
        this.taskId = taskId;
        this.similarInstruct1 = similarInstruct1;
        this.similarInstruct2 = similarInstruct2;
        this.input = input;
        this.output = output;
    }


    //=== 연관관계 편의메서드 ===//
    public void addUser(User user){
        this.user = user;
        user.getAssignList().add(this);
    }

    public void addIOPair(IOPairs ioPairs){
        this.ioPairs = ioPairs;
        ioPairs.addAssignment(this);
    }


    //=== 비지니스 코드 ===//
    // 유사 지시문1 & 2 갱신
    public void updateSimilarInstruct(AssignRequest assignRequest){
        if (assignRequest.getSimilarInstruct1() != null){
            if (!assignRequest.getSimilarInstruct1().equals("null")){
                this.similarInstruct1 = assignRequest.getSimilarInstruct1();
            }
        }
        // 갱신조건: null이 아닐 때, "null"이 아닐 때
        if (assignRequest.getSimilarInstruct2() != null) {
            if (!assignRequest.getSimilarInstruct2().equals("null")){
                this.similarInstruct2 = assignRequest.getSimilarInstruct2();
            }
        }
    }
    // task sub index 설정
    public void addTaskSubIndex(Long taskSubIdx){
        this.taskSubIdx = taskSubIdx;
    }
    // 입력 & 출력 갱신
    public void updateIO(String input, String output){
        if (input != null){
            if (!input.equals("null")){
                this.input = input;
            }
        }
        if (output != null){
            if (!output.equals("null")){
                this.output = output;
            }
        }
    }
}
