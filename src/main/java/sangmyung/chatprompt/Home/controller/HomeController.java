package sangmyung.chatprompt.Home.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sangmyung.chatprompt.Util.XmlParser;
import sangmyung.chatprompt.xml.DTO.PromptListDTO;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private XmlParser parser = new XmlParser();


    @GetMapping("/")
    public String initPage(){

        return "editDefinition";
    }


    @GetMapping("/parse")
    public String parseXml() throws JAXBException, IOException {
        PromptListDTO unmarshall = parser.unmarshall();
        log.info("success");


        return "editDefinition";
    }
}
