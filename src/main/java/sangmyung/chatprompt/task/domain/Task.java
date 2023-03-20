package sangmyung.chatprompt.task.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import sangmyung.chatprompt.user.domain.User;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<IOPairs> ioPairsList = new ArrayList<>();

    private int taskNum; // 179
    private String taskStr; // ex) task179_participant_extraction

    private String category; // Information Extraction

    @Column(columnDefinition = "TEXT")
    private String definition_eng; // definition 영어
    @Column(columnDefinition = "TEXT")
    private String definition_kor; // definition 한글
    private String type; // ex) example, instance

    private int numInputTokens;


    @Builder
    public Task(int taskNum, String taskStr, String category, String definition_eng, String definition_kor,
                String type, int numInputTokens) {
        this.taskNum = taskNum;
        this.taskStr = taskStr;
        this.category = category;
        this.definition_eng = definition_eng;
        this.definition_kor = definition_kor;
        this.type = type;
        this.numInputTokens = numInputTokens;
    }

    //== 연관관계 편의메서드 ==//
    public void addUser(User user){
        this.user = user;
        user.getTaskList().add(this);
    }
}
