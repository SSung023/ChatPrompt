package sangmyung.chatprompt.xml.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


@XmlAccessorType(XmlAccessType.FIELD)
@Getter @Setter
@ToString
public class PromptDTO {

    @XmlElement(name = "index_SMU")
    private int id;

    @XmlElement(name = "line_num")
    private String num;
    @XmlElement(name = "category")
    private String category;
    @XmlElement(name = "task")
    private String task;
    @XmlElement(name = "idx")
    private String index;
    @XmlElement(name = "type")
    private String type;
    @XmlElement(name = "definition")
    private String definition;
    @XmlElement(name = "input")
    private String inputStr;
    @XmlElement(name = "output")
    private String outputStr;
    @XmlElement(name = "num_input_tokens")
    private int inputToken;


}
