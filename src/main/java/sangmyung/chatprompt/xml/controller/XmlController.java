package sangmyung.chatprompt.xml.controller;

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
import sangmyung.chatprompt.xml.service.XmlExtractor;

import javax.xml.bind.JAXBException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class XmlController {
    private final XmlExtractor extractor;

    @GetMapping("/extract/instruct")
    public CommonResponse extractInstructions
            (@PageableDefault(size = 10, sort = "taskSubIdx", direction = Sort.Direction.ASC) Pageable pageable) throws JAXBException {
        extractor.extractInstruct(pageable);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }

    @GetMapping("/extract/io")
    public CommonResponse extractIOPairs
            (@PageableDefault(size = 60, sort = "ioPairsIdx", direction = Sort.Direction.ASC) Pageable pageable) throws JAXBException {

        extractor.extractIOPair(pageable);

        return new CommonResponse(SuccessCode.SUCCESS.getStatus(), SuccessCode.SUCCESS.getMessage());
    }
}
