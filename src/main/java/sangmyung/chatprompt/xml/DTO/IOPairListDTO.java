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
public class IOPairListDTO {
    @XmlElement(name = "IOPairDTO")
    private List<IOPairDTO> ioPairDTOList;

    @Builder
    public IOPairListDTO(List<IOPairDTO> ioPairDTOList) {
        this.ioPairDTOList = ioPairDTOList;
    }
}
