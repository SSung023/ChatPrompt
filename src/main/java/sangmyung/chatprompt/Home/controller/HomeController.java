package sangmyung.chatprompt.Home.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sangmyung.chatprompt.Util.XmlParser;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private XmlParser parser = new XmlParser();


    @GetMapping("/")
    public String test(Model model) throws JAXBException, IOException {
//        Home home = new Home("test");
//        repository.save(home);
        PromptDTO unmarshall = parser.unmarshall();
        log.info(unmarshall.toString());

        return "editDefinition";
    }
}
