package sangmyung.chatprompt.xml.DTO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Root")
@Getter @Setter
@ToString
public class PromptListDTO {
    @XmlElement(name = "text")
    private PromptDTO[] infoList;
}
