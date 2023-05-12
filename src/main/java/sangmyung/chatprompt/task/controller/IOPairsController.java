package sangmyung.chatprompt.task.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import sangmyung.chatprompt.Util.exception.BusinessException;
import sangmyung.chatprompt.Util.exception.ErrorCode;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.Util.response.dto.ListResponse;
import sangmyung.chatprompt.Util.response.dto.SingleResponse;
import sangmyung.chatprompt.task.dto.ValidationIOResponse;
import sangmyung.chatprompt.task.dto.ValidationResponse;
import sangmyung.chatprompt.task.service.IoPairService;
import sangmyung.chatprompt.user.domain.User;
import sangmyung.chatprompt.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class IOPairsController {
    private final UserService userService;
    private final IoPairService ioPairService;



    @GetMapping("/verifications/tasks/{taskId}/io/lists")
    public ListResponse<ValidationIOResponse> getVerifiedIOLists(HttpServletRequest request, @PathVariable Long taskId
                        ,@PageableDefault(size = 60, sort = "ioPairsIdx", direction = Sort.Direction.ASC) Pageable pageable){
        // 사용자 정보를 받음
        User user = validateTaskIdxAndGetUser(request, taskId);

        List<ValidationIOResponse> ioPairList = ioPairService.getIOPairValidationList(user.getId(), taskId, pageable);

        return new ListResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), ioPairList);
    }

    /**
     * 특정 Task의 특정 입출력의 검증 여부를 반환
     */
    @GetMapping("/verifications/tasks/{taskId}/io/{ioIndex}")
    public SingleResponse<ValidationIOResponse> getIOVerified(HttpServletRequest request,
                                        @PathVariable Long taskId, @PathVariable int ioIndex){
        // 사용자 정보를 받음
        User user = validateTaskIdxAndGetUser(request, taskId);

        // 해당하는 assignment를 찾고 validate 여부를 갱신 후, Response 객체 받기
        ValidationIOResponse ioResponse = ioPairService.getIsIOValidated(user.getId(), taskId, ioIndex);

        return new SingleResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), ioResponse);
    }

    /**
     * 특정 Task의 특정 입출력의 검증 여부를 갱신
     * /api/verification/tasks/{taskId}/io/{ioIndex}?verify=yes
     * /api/verification/tasks/{taskId}/io/{ioIndex}?verify=no
     */
    @PatchMapping("/verifications/tasks/{taskId}/io/{ioIndex}")
    public SingleResponse<ValidationResponse> updateVerifyStatus(HttpServletRequest request, @RequestParam String verify,
                                        @PathVariable Long taskId, @PathVariable int ioIndex){
        // 사용자 정보를 받음
        User user = validateTaskIdxAndGetUser(request, taskId);

        ValidationResponse validationResponse = ioPairService.updateIOValidated(user.getId(), taskId, ioIndex, verify);

        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), validationResponse);
    }

    /** 특정 Task의 현재 검증 진행율을 반환 - userId, taskId 필요
     * GET /api/verifications/tasks/{taskId}
     */
    @GetMapping("/verifications/tasks/{taskId}")
    public SingleResponse<ValidationResponse> getCurValidatedCnt(HttpServletRequest request, @PathVariable Long taskId){
        // 사용자 정보를 받음
        User user = validateTaskIdxAndGetUser(request, taskId);

        ValidationResponse curValidatedIOCnt = ioPairService.getCurValidatedIOCnt(user.getId(), taskId);
        return new SingleResponse<>(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage(), curValidatedIOCnt);
    }

    @PatchMapping("/verifications/tasks")
    public CommonResponse resetVerifyStatus(){
        ioPairService.resetIOPairVerifyStatus();

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }



    private User validateTaskIdxAndGetUser(HttpServletRequest request, Long taskId) {
        // Session에서 User의 정보를 얻음
        Long userId = userService.getUserIdFromSession(request);
        if (userId == null){
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        User user = userService.findUserById(userId);

        // 사용자가 할당받은 TaskId가 아니라면 Exception 발생
        if (!userService.isAssignedTaskNum(user, taskId)){
            throw new BusinessException(ErrorCode.INVALID_PARAMETER);
        }
        return user;
    }
}
