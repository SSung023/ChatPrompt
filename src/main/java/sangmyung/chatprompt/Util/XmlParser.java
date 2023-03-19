package sangmyung.chatprompt.Util;

import sangmyung.chatprompt.xml.DTO.PromptDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;

public class XmlParser {

    public PromptDTO unmarshall() throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(PromptDTO.class);
        return (PromptDTO) context.createUnmarshaller()
                .unmarshal(new FileReader("prompt/preparation/reference.xml"));
    }
}
