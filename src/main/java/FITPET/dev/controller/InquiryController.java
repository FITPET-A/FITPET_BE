package FITPET.dev.controller;

import FITPET.dev.common.basecode.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.InquiryRequest;
import FITPET.dev.service.InquiryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("")
    public ApiResponse postInquiry(@RequestBody InquiryRequest.InquiryDto inquiryDto){
        inquiryService.postInquiry(inquiryDto);
        return ApiResponse.SuccessResponse(SuccessStatus.POST_INQUIRY);
    }
}
