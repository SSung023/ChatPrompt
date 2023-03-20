package sangmyung.chatprompt.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.task.service.TaskService;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;


    @GetMapping("/parse")
    public CommonResponse parseXml() throws JAXBException, IOException {
        String xmlPath = "prompt/preparation/reference.xml";
        String path = "prompt/preparation/sample.xml";

        taskService.parseXmlToTask(path);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }
}
