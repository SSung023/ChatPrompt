package sangmyung.chatprompt.xml.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "InstructDTO")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public class InstructDTO {
    // 교수님 수정 목적 필드
    @XmlElement(name = "assignedTaskId")
    private String assignedTaskId;


    @XmlElement(name = "line_num")
    private String lineNum;

    @XmlElement(name = "task")
    private String task;

    @XmlElement(name = "definition")
    private String definition;


    @Builder
    public InstructDTO(String lineNum, String task, String definition, String assignedTaskId) {
        this.lineNum = lineNum;
        this.task = task;
        this.definition = definition;

        this.assignedTaskId = assignedTaskId;
    }
}
