package FITPET.dev.controller;

import FITPET.dev.common.enums.ComparisonStatus;
import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.ComparisonRequest;
import FITPET.dev.service.ComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


@Tag(name = "견적서 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ComparisonController {

    private final ComparisonService comparisonService;

    @PostMapping("/comparison")
    @Operation(summary = "견적서 생성 API", description = "반려 동물 정보 및 사이트 유입 정보를 받아 견적 요청 테이블 생성")
    public ApiResponse<?> createComparisonAndGetInsurance(@RequestBody ComparisonRequest comparisonRequest) {
        return ApiResponse.SuccessResponse(SuccessStatus.POST_COMPARISON,
                comparisonService.createComparisonAndGetInsurance(comparisonRequest));
    }


    /*
     * admin
     */
    @GetMapping("/admin/comparison")
    @Operation(summary = "견적 요청 리스트 조회 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 견적 요청 정보를 조회")
    public ApiResponse getComparisons(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "status", required = false, defaultValue = "all") String status
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_COMPARISON_TABLE, comparisonService.getComparisons(startDate, endDate, page, status));
    }


    @GetMapping("/admin/comparison/downloads")
    @Operation(summary = "견적 요청 리스트 엑셀 다운로드 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 견적 요청 정보를 가진 엑셀을 다운로드")
    public void downloadComparisons(HttpServletResponse servletResponse,
                                 @RequestParam(name = "startDate", required = false) String startDate,
                                 @RequestParam(name = "endDate", required = false) String endDate,
                                 @RequestParam(name = "status", required = false, defaultValue = "all") String status) {
        comparisonService.downloadComparisons(servletResponse, startDate, endDate, status);
    }


    @GetMapping("/admin/comparison/{comparisonId}/pdf")
    @Operation(summary = "견적서 pdf 다운로드 API", description = "견적 요청 정보의 id를 받아, 해당 견적서를 생성해 pdf 다운로드")
    public void downloadComparisonPdf(HttpServletResponse servletResponse,
                                 @PathVariable(value = "comparisonId") Long comparisonId
    ) {
        comparisonService.downloadComparisonPdf(servletResponse, comparisonId);
    }


    @GetMapping("/admin/comparison/search")
    @Operation(summary = "견적서 검색 API", description = "견적서 검색")
    public ApiResponse searchPetInfos(
            @RequestParam(name = "content", required = false) String content,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.SEARCH_COMPARISON, comparisonService.searchComparisons(content, page));
    }


    @PatchMapping("/admin/comparison/status/{comparisonId}")
    @Operation(summary = "견적 요청 상태 변경 API", description = "특정 견적 요청 정보의 상태값을 변경")
    public ApiResponse patchPetInfoStatus(
            @PathVariable(value = "comparisonId") Long comparisonId,
            @RequestParam(name = "status") ComparisonStatus status
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.PATCH_COMPARISON_STATUS, comparisonService.patchComparisonStatus(comparisonId, status));
    }


    @DeleteMapping("/admin/comparison/{comparisonId}")
    @Operation(summary = "견적 요청 삭제 API", description = "특정 견적 요청 정보를 삭제")
    public ApiResponse deleteComparison(
            @PathVariable(value = "comparisonId") Long comparisonId
            ) {
        comparisonService.deleteComparison(comparisonId);
        return ApiResponse.SuccessResponse(SuccessStatus.DELETE_COMPARISON);
    }

}
