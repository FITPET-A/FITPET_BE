package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.InsuranceRequest;
import FITPET.dev.dto.response.InsuranceHistoryResponse;
import FITPET.dev.service.InsuranceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "보험 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class InsuranceController {
    private final InsuranceService insuranceService;

    @GetMapping("/insurance")
    @Operation(summary = "보험료 조회 API", description = "세부품종, 나이, 갱신주기, 보상비율, 자부담, 1일 보상 금액 정보를 Parameter으로 받아 각 회사의 보험료와 할인 보험료 정보를 조회")
    public ApiResponse getInsurancePremium(@RequestParam(name = "petSpecies") String petSpecies,
                                           @RequestParam(name = "age", defaultValue = "0") int age,
                                           @RequestParam(name = "renewalCycle", defaultValue = "3년", required = false) String renewalCycle,
                                           @RequestParam(name = "coverageRatio", defaultValue = "70", required = false) String coverageRatio,
                                           @RequestParam(name = "deductible", defaultValue = "1만원", required = false) String deductible,
                                           @RequestParam(name = "compensation", defaultValue = "15만", required = false) String compensation
                                           ){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_PREMIUM,
                insuranceService.getInsurancePremium(petSpecies, age, renewalCycle, coverageRatio, deductible, compensation));
    }

    /*
     * admin
     */
    @GetMapping("/admin/insurance")
    @Operation(summary = "회사별 보험 정보 조회 API", description = "회사명을 Parameter으로 받아 각 회사의 보험 정보를 조회")
    public ApiResponse getInsurances(
            @RequestParam(name = "company", required = false, defaultValue = "all") String company,
            @RequestParam(name = "petType", required = false, defaultValue = "all") String petType,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_TABLE, insuranceService.getInsurances(page, company, petType));
    }

    @GetMapping("/admin/insurance/downloads")
    @Operation(summary = "회사별 보험 정보 엑셀 다운로드 API", description = "각 회사의 보험 정보를 가진 엑셀을 다운로드")
    public void downloadInsurances(HttpServletResponse servletResponse) {
        insuranceService.downloadInsurances(servletResponse);
    }

    @PatchMapping("/admin/insurance/{insuranceId}")
    @Operation(summary = "보험료 수정 API", description = "특정 보험id의 보험료를 수정")
    public ApiResponse updateInsurance(
            @PathVariable(value = "insuranceId") Long insuranceId,
            @RequestParam(name = "premium") int premium
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.UPDATE_INSURANCE_SUCCESS, insuranceService.updateInsurancePremium(insuranceId, premium));
    }

    @GetMapping("/admin/insurance/premium/{insuranceId}")
    @Operation(summary = "보험료 수정 내역 조회 API", description = "특정 보험id의 보험료 수정 내역을 조회")
    public ApiResponse getPremiumHistory(
            @PathVariable(name = "insuranceId") Long insuranceId
    ) {
        List<InsuranceHistoryResponse> response = insuranceService.getPremiumHistory(insuranceId);
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_PREMIUM_HISTORY, response);
    }

    @PostMapping("/admin/insurance/add")
    @Operation(summary = "보험 정보 추가 API", description = "새로운 보험 정보를 추가")
    public ApiResponse addInsurance(
            @RequestBody InsuranceRequest request
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.ADD_INSURANCE_SUCCESS, insuranceService.addInsurance(request));
    }

    // 보험 정보 삭제 API
    @DeleteMapping("/admin/insurance/delete/{insuranceId}")
    @Operation(summary = "보험 정보 삭제 API", description = "해당 보험 id의 보험 정보를 삭제")
    public ApiResponse deleteInsurance(@PathVariable Long insuranceId) {
        insuranceService.deleteInsurance(insuranceId);
        return ApiResponse.SuccessResponse(SuccessStatus.DELETE_INSURANCE_SUCCESS);
    }

    // 삭제된 보험 정보 전체 조회 API
    @GetMapping("/admin/insurance/deleted")
    @Operation(summary = "삭제된 보험 정보 전체 조회 API", description = "삭제된 보험 정보 조회")
    public ApiResponse getDeletedInsurances(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_DELETED_INSURANCES, insuranceService.getDeletedInsurances(page));
    }

}
