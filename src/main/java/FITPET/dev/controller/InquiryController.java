package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.InquiryRequest;
import FITPET.dev.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("")
    @Operation(summary = "1:1 문의 전송 API", description = "이름, 이메일, 전화번호, 내용을 Parameter으로 받아 1:1 문의를 전송")
    public ApiResponse postInquiry(@RequestBody InquiryRequest.InquiryDto inquiryDto){
        return ApiResponse.SuccessResponse(SuccessStatus.POST_INQUIRY, inquiryService.postInquiry(inquiryDto));
    }
}
