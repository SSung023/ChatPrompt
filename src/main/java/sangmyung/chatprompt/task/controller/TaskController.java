package sangmyung.chatprompt.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.Util.response.dto.ListResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.task.dto.IOResponse;
import sangmyung.chatprompt.task.dto.TaskResponse;
import sangmyung.chatprompt.task.service.TaskService;
import sangmyung.chatprompt.user.dto.UserRequest;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class TaskController {
    private final TaskService taskService;


    /**
     * 특정 xml 파일을 파싱하여 Entity에 mapping
     *
     */
    @GetMapping("/parse")
    public CommonResponse parseXml() throws JAXBException, IOException {
        String xmlPath = "prompt/preparation/reference.xml";
        String path = "prompt/preparation/sample.xml";

        taskService.parseXmlToTask(xmlPath);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    /**
     * 특정 사용자 선택과 함께, 사용자가 마지막으로 수정한 Task의 PK를 반환
     * @param userRequest 사용자의 실명이 담긴 DTO
     */
    @PostMapping("/login")
    public SingleResponse<Long> userLogin(@RequestBody UserRequest userRequest){
        String username = userRequest.getUsername(); // 실명이 담긴 객체
        Long lastModifiedTaskId = taskService.getLastModifiedTaskId(username);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), lastModifiedTaskId);
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
}
