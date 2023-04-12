package sangmyung.chatprompt.oursource.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sangmyung.chatprompt.Util.exception.SuccessCode;
import sangmyung.chatprompt.Util.response.dto.CommonResponse;
import sangmyung.chatprompt.oursource.service.OutsourceService;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class OutsourceController {
    private final OutsourceService outsourceService;


    @GetMapping("/outsource/type0")
    public CommonResponse getType0IOAssignment(@PageableDefault(size = 60, sort = "idx", direction = Sort.Direction.ASC) Pageable pageable){
        outsourceService.extractType0(pageable);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/outsource/type1-simple")
    public CommonResponse getType1_Simple(@PageableDefault(size = 60, sort = "idx", direction = Sort.Direction.ASC) Pageable pageable) {
        outsourceService.extractType1_SimpleConvert(pageable);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/outsource/type1-complex")
    public CommonResponse getType1_Complex(@PageableDefault(size = 60, sort = "idx", direction = Sort.Direction.ASC) Pageable pageable) {
        outsourceService.extractType1(pageable);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }
}
