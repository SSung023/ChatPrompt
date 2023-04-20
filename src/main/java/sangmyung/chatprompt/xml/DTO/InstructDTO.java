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
    @XmlElement(name = "line_num")
    private String lineNum;

    @XmlElement(name = "task")
    private String task;

    @XmlElement(name = "definition")
    private String definition;


    @Builder
    public InstructDTO(String lineNum, String task, String definition) {
        this.lineNum = lineNum;
        this.task = task;
        this.definition = definition;
    }
}
