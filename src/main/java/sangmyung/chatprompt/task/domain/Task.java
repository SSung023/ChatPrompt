package sangmyung.chatprompt.task.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<IOPairs> ioPairsList = new ArrayList<>();

    private int taskNum; // 179
    private String taskStr; // ex) task179_participant_extraction

    private String category; // Information Extraction


    @Column(columnDefinition = "TEXT")
    private String definition1; // definition1 : 지시문 원문(윤문)
    @Column(columnDefinition = "TEXT")
    private String definition2; // definition1 : 기계번역문
    private String type; // ex) example, instance

    private int totalIoNum; // IOPair 쌍의 최대 수 -> 1~ 33(102)
    private int numInputTokens;


    @Builder
    public Task(int taskNum, String taskStr, String category,
                String definition1, String definition2,
                String type, int totalIoNum, int numInputTokens) {
        this.taskNum = taskNum;
        this.taskStr = taskStr;
        this.category = category;
        this.definition1 = definition1;
        this.definition2 = definition2;
        this.type = type;
        this.totalIoNum = totalIoNum;
        this.numInputTokens = numInputTokens;
    }

}
