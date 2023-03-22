package sangmyung.chatprompt.task.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sangmyung.chatprompt.assignment.domain.Assignment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IOPairs {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "io_pairs_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Task task;

    @OneToOne(mappedBy = "ioPairs")
    private Assignment assignment;

    private int idx; // 1a, 1b에서의 숫자(index)

    @Column(columnDefinition = "TEXT")
    private String input1;
    @Column(columnDefinition = "TEXT")
    private String output1;
    @Column(columnDefinition = "TEXT")
    private String input2;
    @Column(columnDefinition = "TEXT")
    private String output2;


    @Builder
    public IOPairs(int idx, String input1, String output1, String input2, String output2) {
        this.idx = idx;
        this.input1 = input1;
        this.output1 = output1;
        this.input2 = input2;
        this.output2 = output2;
    }

    //== 연관관계 편의메서드 ==//
    public void addTask(Task task){
        this.task = task;
        task.getIoPairsList().add(this);
    }

    public void addAssignment(Assignment assignment){
        this.assignment = assignment;
    }
}
