package sangmyung.chatprompt.task.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class IOPairs {
    @Id @GeneratedValue
    @Column(name = "io_pairs_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id")
    private Task task;

    private int idx; // 1a, 1b에서의 숫자(index)

    private String input1;
    private String output1;
    private String input2;
    private String output2;


    //== 연관관계 편의메서드 ==//
    public void addTask(Task task){
        this.task = task;
        task.getIoPairsList().add(this);
    }
}
