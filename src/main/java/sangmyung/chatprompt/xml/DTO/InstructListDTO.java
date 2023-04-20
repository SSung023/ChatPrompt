package sangmyung.chatprompt.xml.DTO;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@Data
@ToString
@NoArgsConstructor
public class InstructListDTO {

    @XmlElement(name = "InstructDTO")
    private List<InstructDTO> instructOutputs;

    @Builder
    public InstructListDTO(List<InstructDTO> instructOutputs) {
        this.instructOutputs = instructOutputs;
    }
}
