package sangmyung.chatprompt.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.Util.response.dto.ListResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.assignment.service.AssignmentService;
import sangmyung.chatprompt.task.dto.IOResponse;
import sangmyung.chatprompt.task.dto.SingleIOResponse;
import sangmyung.chatprompt.task.dto.TaskResponse;
import sangmyung.chatprompt.task.service.TaskService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TaskController {
    private final UserService userService;
    private final TaskService taskService;
    private final AssignmentService assignService;


    /**
     * 특정 xml 파일을 파싱하여 Entity에 mapping
     */
    @GetMapping("/parse")
    public CommonResponse parseXml() throws JAXBException, IOException {
        String xmlPath = "prompt/preparation/reference.xml";
        String path = "prompt/preparation/sample.xml";

        taskService.parseXmlToTask(xmlPath);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/parse/test")
    public CommonResponse parseTestFile() throws JAXBException, IOException {
        String path = "prompt/preparation/sample.xml";

        taskService.parseXmlToTask(path);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }




    /**
     * 특정 Task의 Definition 정보(엑셀 파일에 있던) 반환
     * @param taskId Definition 정보를 얻고 싶은 Task의 PK
     */
    @GetMapping("/tasks/{taskId}")
    public SingleResponse<TaskResponse> getTaskDefinition(HttpServletRequest request, @PathVariable Long taskId){
        // Session에서 User의 정보(PK)를 얻음
        Long userId = userService.getUserIdFromRequest(request);

        User user = userService.findUserById(userId);

        // Assignment(userId=1)의 definition1&2를 받아서 줘야한다.
        TaskResponse taskResponse = taskService.getTaskDefinition(user, taskId);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), taskResponse);
    }

    /**
     * 특정 Task의 Definition 정보(지시문 원문(윤문) & 기계번역문)를 반환
     * @param taskId Definition 정보를 얻고 싶은 Task PK
     */
    @GetMapping("/tasks/{taskId}/definitions")
    public SingleResponse<TaskResponse> getModifiedDefinition(HttpServletRequest request, @PathVariable Long taskId){
        // Session에서 User의 정보(PK)를 얻음
        Long userId = userService.getUserIdFromRequest(request);

        User user = userService.findUserById(userId);
        Long proId = userService.findUserByUserName("박소영").getId();

        // Assignment(userId=1)의 definition1&2를 받아서 줘야한다.
        TaskResponse taskResponse = assignService.getDefinitions(proId, taskId);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), taskResponse);
    }

    /**
     * 특정 Task의 input&output 정보를 List로 반환
     * @param taskId 입출력쌍 정보를 얻고 싶은 Task의 PK
     */
    @GetMapping("/tasks/{taskId}/io-pairs")
    public ListResponse<IOResponse> getTaskIOPairs(@PathVariable Long taskId){
        List<IOResponse> ioPairs = taskService.getTaskIOPairs(taskId);

        return new ListResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), ioPairs);
    }


    /**
     * 특정 Task PK의 특정 인덱스의 입출력쌍 정보(IOPair)의 정보를 반환
     * @param taskId 특정 Task PK
     * @param ioIndex 특정 Task의 특정 입출력 세트(IOPairs)의 Index
     */
    @GetMapping("/tasks/{taskId}/io-pairs/{ioIndex}")
    public SingleResponse<SingleIOResponse> getSingleIOPair(@PathVariable Long taskId, @PathVariable int ioIndex){

        SingleIOResponse singleIOResponse = taskService.getCertainIOPairs(taskId, ioIndex);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), singleIOResponse);
    }

}
