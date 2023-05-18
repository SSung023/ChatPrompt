package sangmyung.chatprompt.xml.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IOPairDTO")
@XmlAccessorType(XmlAccessType.NONE)
@Data
@NoArgsConstructor
public class IOPairDTO {
    @XmlElement(name = "line_num")
    private String lineNum;

    @XmlElement(name = "category")
    private String category;

    @XmlElement(name = "task")
    private String taskName;

    @XmlElement(name = "idx")
    private String idx;

    @XmlElement(name = "type")
    private String type;

    @XmlElement(name = "definition")
    private String definition;

    @XmlElement(name = "input")
    private String input;

    @XmlElement(name = "output")
    private String output;


    @Builder
    public IOPairDTO(String lineNum, String category, String taskName, String idx, String type, String definition, String input, String output) {
        this.lineNum = lineNum;
        this.category = category;
        this.taskName = taskName;
        this.idx = idx;
        this.type = type;
        this.definition = definition;
        this.input = input;
        this.output = output;
    }
}
