package sangmyung.chatprompt.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.Util.response.dto.ListResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.task.dto.DefRequest;
import sangmyung.chatprompt.task.dto.IOResponse;
import sangmyung.chatprompt.task.dto.TaskResponse;
import sangmyung.chatprompt.task.service.TaskService;
import sangmyung.chatprompt.user.dto.UserRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

import static sangmyung.chatprompt.Util.session.SessionConst.LOGIN_MEMBER_PK;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;


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
     * 특정 Task의 Definition 정보(한글, 영어)를 반환
     * @param taskId Definition 정보를 얻고 싶은 Task의 PK
     */
    @GetMapping("/tasks/{taskId}")
    public SingleResponse<TaskResponse> getTaskDefinition(@PathVariable Long taskId){
        TaskResponse taskResponse = taskService.getTaskDefinition(taskId);

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
     * 관리자가 특정 Task의 Definition을 등록(변경) 요청
     * @param taskId 지시문을 변경하고자 하는 Task의 PK
     * @param defRequest 변경하고자 하는 지시문 내용
     * 이전 api : /tasks/{taskId}/users/{userId}/instruction
     */
    @PatchMapping("/tasks/{taskId}/instruction")
    public SingleResponse<TaskResponse> registerNewDefinition
        (HttpServletRequest request, @PathVariable Long taskId, @RequestBody DefRequest defRequest){

        // Session에서 User의 정보(UserId)를 얻음
        HttpSession session = request.getSession(false);
        Long userId = (Long) session.getAttribute(LOGIN_MEMBER_PK);

        TaskResponse taskResponse = taskService.updateDefinition(taskId, userId, defRequest);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), taskResponse);
    }
}
