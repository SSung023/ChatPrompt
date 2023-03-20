package sangmyung.chatprompt.task.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sangmyung.chatprompt.task.repository.TaskRepository;
import sangmyung.chatprompt.xml.DTO.PromptListDTO;
import sangmyung.chatprompt.xml.service.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final XmlParser xmlParser;


    // xml 파일을 promptDTO로 변환 후, Task&IOPairs 테이블에 정보 저장
    @Transactional
    public void parseXmlToTask() throws JAXBException, IOException {
        // xml에서
        PromptListDTO promptList = xmlParser.unmarshall();

    }

}
