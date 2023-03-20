package sangmyung.chatprompt.task.domain;

import lombok.AccessLevel;
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
    @GeneratedValue
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

    private String definition_eng; // definition 영어
    private String definition_kor; // definition 한글
    private String type; // ex) example, instance

    private int numInputTokens;



    //== 연관관계 편의메서드 ==//
    public void addUser(User user){
        this.user = user;
        user.getTaskList().add(this);
    }
}
