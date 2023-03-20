package sangmyung.chatprompt.xml.service;

import org.springframework.stereotype.Service;
import sangmyung.chatprompt.xml.DTO.PromptListDTO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.FileReader;
import java.io.IOException;

@Service
public class XmlParser {
    private String xmlPath = "prompt/preparation/reference.xml";

    public PromptListDTO unmarshall(String path) throws JAXBException, IOException {
        JAXBContext context = JAXBContext.newInstance(PromptListDTO.class);
        return (PromptListDTO) context.createUnmarshaller()
                .unmarshal(new FileReader(path));
    }
}
