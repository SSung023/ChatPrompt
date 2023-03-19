package sangmyung.chatprompt.Home.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import sangmyung.chatprompt.Util.TxtAppender;
import sangmyung.chatprompt.Util.TxtWriter;
import sangmyung.chatprompt.Util.XmlParser;
import sangmyung.chatprompt.xml.DTO.PromptDTO;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private XmlParser parser = new XmlParser();
    private final TxtWriter txtWriter;
    private final TxtAppender txtAppender;


    @GetMapping("/")
    public String initPage(){

        return "editDefinition";
    }


    @GetMapping("/parse")
    public String writeTxt() throws JAXBException, IOException {
        Set<String> taskNumList = new HashSet<>();
        List<PromptDTO> infoList = parser.unmarshall().getInfoList();
        int len = infoList.size();

        for (int i = 0; i < len; i+=2){
            List<PromptDTO> promptList = new ArrayList<>();
            promptList.add(infoList.get(i));
            promptList.add(infoList.get(i+1));

            String taskNum = promptList.get(0).getTask().replaceAll("[^0-9]", ""); // 179와 같은 task의 번호
            String idx = infoList.get(i).getIndex().replaceAll("[^0-9]", ""); // 1a, 1b와 같은 번호

            taskNumList.add(taskNum);
            txtWriter.checkAndWriteFile(promptList, taskNum, idx);
        }

        for (String taskNum : taskNumList) {
            txtWriter.addTableSuffix(taskNum);
        }

        return "editDefinition";
    }
}
