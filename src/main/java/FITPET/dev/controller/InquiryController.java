package FITPET.dev.controller;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.InquiryRequest;
import FITPET.dev.service.InquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "1:1 문의 전송 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping("/inquiry")
    @Operation(summary = "1:1 문의 전송 API", description = "이름, 이메일, 전화번호, 내용을 Parameter으로 받아 1:1 문의를 전송")
    public ApiResponse postInquiry(@RequestBody InquiryRequest.InquiryDto inquiryDto){
        return ApiResponse.SuccessResponse(SuccessStatus.POST_INQUIRY, inquiryService.postInquiry(inquiryDto));
    }


    /*
     * admin
     */
    @GetMapping("/admin/inquiry")
    @Operation(summary = "1:1 문의 내역 조회 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 1:1 문의 내역 조회")
    public ApiResponse getInquiries(
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "status", required = false) InquiryStatus inquiryStatus
    ){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INQUIRY, inquiryService.getInquiries(startDate, endDate, inquiryStatus));
    }

    @PatchMapping("/admin/inquiry/status/{inquiryId}")
    @Operation(summary = "1:1 문의 상태 변경 API", description = "특정 1:1 문의 정보의 상태값을 변경")
    public ApiResponse patchInquiryStatus(
            @PathVariable(value = "inquiryId") Long inquiryId,
            @RequestParam(name = "status") InquiryStatus inquiryStatus
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.PATCH_COMPARISON_STATUS, inquiryService.patchInquiryStatus(inquiryId, inquiryStatus));
    }
}
