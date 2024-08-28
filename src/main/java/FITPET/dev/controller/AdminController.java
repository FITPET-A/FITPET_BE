package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.enums.Status;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.service.AdminService;
import FITPET.dev.service.InitService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/insurance")
    @Operation(summary = "회사별 보험 정보 조회 API", description = "회사명을 Parameter으로 받아 각 회사의 보험 정보를 조회")
    public ApiResponse getInsurances(
            @RequestParam(name = "company", required = false, defaultValue = "all") String company,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_TABLE, adminService.getInsurances(page, company));
    }


    @GetMapping("/insurance/downloads")
    @Operation(summary = "회사별 보험 정보 엑셀 다운로드 API", description = "회사명을 Parameter으로 받아 각 회사의 보험 정보를 가진 엑셀을 다운로드")
    public void downloadInsurances(
            HttpServletResponse servletResponse,
            @RequestParam(name = "company", required = false, defaultValue = "all") String company
    ) {
        adminService.downloadInsurances(servletResponse, company);
    }


    @GetMapping("/petinfo")
    @Operation(summary = "견적 요청 리스트 조회 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 견적 요청 정보를 조회")
    public ApiResponse getPetInfos(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "startDate") String startDate,
            @RequestParam(name = "endDate") String endDate,
            @RequestParam(name = "status", required = false, defaultValue = "PENDING") Status status
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_PETINFO_TABLE, adminService.getPetInfos(startDate, endDate, page, status));
    }


    @GetMapping("/petinfo/downloads")
    @Operation(summary = "견적 요청 리스트 엑셀 다운로드 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 견적 요청 정보를 가진 엑셀을 다운로드")
    public void downloadPetInfos(HttpServletResponse servletResponse,
                                 @RequestParam(name = "startDate") String startDate,
                                 @RequestParam(name = "endDate") String endDate,
                                 @RequestParam(name = "status", required = false, defaultValue = "PENDING") Status status) {
        adminService.downloadPetInfos(servletResponse, startDate, endDate, status);
    }


    @PatchMapping("/petinfo/status/{petInfoId}")
    @Operation(summary = "견적 요청 상태 변경 API", description = "특정 견적 요청 정보의 상태값을 변경")
    public ApiResponse patchPetInfoStatus(
            @PathVariable(value = "petInfoId") Long petInfoId,
            @RequestParam(name = "status") Status status
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.PATCH_PET_INFO_STATUS, adminService.patchPetInfoStatus(petInfoId, status));
    }

    @GetMapping("/inquiry")
    @Operation(summary = "1:1 문의 내역 전체 조회 API", description = "1:1 문의 내역 전체 조회")
    public ApiResponse getInquiries(){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INQUIRY, adminService.getInquiries());
    }

    @GetMapping("/proposal")
    @Operation(summary = "제휴 제안 내역 전체 조회 API", description = "제휴 제안 내역 전체 조회")
    public ApiResponse getProposals(){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_PROPOSAL, adminService.getProposals());
    }

    // 전화번호와 펫 이름으로 PetInfo 검색
    @GetMapping("/petinfo/search")
    public ApiResponse searchPetInfos(
            @RequestParam(name = "content", required = false) String content,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.SEARCH_PET_INFO, adminService.searchPetInfos(content, page));
    }

    //보험료 수정
    @PatchMapping("/insurance/{insuranceId}")
    public ApiResponse updateInsurance(
            @PathVariable(value = "insuranceId") Long insuranceId,
            @RequestParam(name = "premium") int premium
    ) {
        adminService.updateInsurance(insuranceId, premium);
        return ApiResponse.SuccessResponse(SuccessStatus.UPDATE_INSURANCE_SUCCESS);
    }
}
