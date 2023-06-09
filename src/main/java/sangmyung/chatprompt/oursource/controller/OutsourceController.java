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

    @GetMapping("/outsource/type2")
    public CommonResponse getType2(@PageableDefault(size = 60, sort = "idx", direction = Sort.Direction.ASC) Pageable pageable){
        outsourceService.extract_C27(pageable);
//
//        outsourceService.extract_D20(pageable);
//
//        outsourceService.extract_E20(pageable);
//        outsourceService.extract_E23(pageable);
//        outsourceService.extract_E26(pageable);
//        outsourceService.extract_E27(pageable);
//        outsourceService.extract_E28(pageable);

//
//        outsourceService.extract_F102(pageable);
//        outsourceService.extract_F105(pageable);
//        outsourceService.extract_F106(pageable);
//        outsourceService.extract_F110(pageable);

//        outsourceService.extract_D46(pageable);
//        outsourceService.extract_D47(pageable);
//        outsourceService.extract_D48(pageable);


        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/outsource/type2-modify")
    public CommonResponse getType2Modified(){
        outsourceService.extract_C22();
        outsourceService.extract_C28();
        outsourceService.extract_C29();
        outsourceService.extract_C30();

        outsourceService.extract_D9();
        outsourceService.extract_D10();
        outsourceService.extract_D11();
        outsourceService.extract_D30();

        outsourceService.extract_E9();
        outsourceService.extract_E10();

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/outsource/type2-convertToKor")
    public CommonResponse convertEngToKor(){
        outsourceService.convertEngToKor();

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }


    @GetMapping("/outsource/check-duplicate")
    public CommonResponse checkIsDuplicateExist(){
        outsourceService.checkDuplicate();

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/outsource/check-duplicate/input")
    public CommonResponse checkIsDuplicateInputOnly(){
        outsourceService.checkDuplicateOnlyInput();

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

}
