package FITPET.dev.controller;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.enums.ComparisonStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.InsuranceRequest;
import FITPET.dev.dto.response.InsuranceHistoryResponse;
import FITPET.dev.service.AdminService;
import FITPET.dev.service.InitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 페이지 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final InitService initService;
    private final AdminService adminService;

    // excel file DB에 저장
    @GetMapping("/init")
    public ApiResponse initDatabase() {
        return ApiResponse.SuccessResponse(SuccessStatus.SUCCESS);
    }


    /*
     * Insurance
     */
    @GetMapping("/insurance")
    @Operation(summary = "회사별 보험 정보 조회 API", description = "회사명을 Parameter으로 받아 각 회사의 보험 정보를 조회")
    public ApiResponse getInsurances(
            @RequestParam(name = "company", required = false, defaultValue = "all") String company,
            @RequestParam(name = "petType", required = false, defaultValue = "all") String petType,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_TABLE, adminService.getInsurances(page, company, petType));
    }


    @GetMapping("/insurance/downloads")
    @Operation(summary = "회사별 보험 정보 엑셀 다운로드 API", description = "각 회사의 보험 정보를 가진 엑셀을 다운로드")
    public void downloadInsurances(HttpServletResponse servletResponse) {
        adminService.downloadInsurances(servletResponse);
    }

    @PatchMapping("/insurance/{insuranceId}")
    @Operation(summary = "보험료 수정 API", description = "특정 보험id의 보험료를 수정")
    public ApiResponse updateInsurance(
            @PathVariable(value = "insuranceId") Long insuranceId,
            @RequestParam(name = "premium") int premium
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.UPDATE_INSURANCE_SUCCESS, adminService.updateInsurancePremium(insuranceId, premium));
    }

    @GetMapping("/insurance/premium/{insuranceId}")
    @Operation(summary = "보험료 수정 내역 조회 API", description = "특정 보험id의 보험료 수정 내역을 조회")
    public ApiResponse getPremiumHistory(
            @PathVariable(name = "insuranceId") Long insuranceId
    ) {
        List<InsuranceHistoryResponse> response = adminService.getPremiumHistory(insuranceId);
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_PREMIUM_HISTORY, response);
    }

    @PostMapping("/insurance/add")
    @Operation(summary = "보험 정보 추가 API", description = "새로운 보험 정보를 추가")
    public ApiResponse addInsurance(
            @RequestBody InsuranceRequest request
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.ADD_INSURANCE_SUCCESS, adminService.addInsurance(request));
    }

    // 보험 정보 삭제 API
    @DeleteMapping("/insurance/delete/{insuranceId}")
    @Operation(summary = "보험 정보 삭제 API", description = "해당 보험 id의 보험 정보를 삭제")
    public ApiResponse deleteInsurance(@PathVariable Long insuranceId) {
        adminService.deleteInsurance(insuranceId);
        return ApiResponse.SuccessResponse(SuccessStatus.DELETE_INSURANCE_SUCCESS);
    }

    // 삭제된 보험 정보 전체 조회 API
    @GetMapping("/insurance/deleted")
    @Operation(summary = "삭제된 보험 정보 전체 조회 API", description = "삭제된 보험 정보 조회")
    public ApiResponse getDeletedInsurances(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_DELETED_INSURANCES, adminService.getDeletedInsurances(page));
    }

    /*
     * Proposal
     */
    @GetMapping("/proposal")
    @Operation(summary = "제휴 제안 내역 전체 조회 API", description = "제휴 제안 내역 전체 조회")
    public ApiResponse getProposals(){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_PROPOSAL, adminService.getProposals());
    }

}
