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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "견적서 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class ComparisonController {

    private final ComparisonService comparisonService;

    @PostMapping("/comparison")
    @Operation(summary = "견적서 생성 API", description = "견적서를 생성하고 기본 보험료를 조회")
    public ApiResponse<?> createComparisonAndGetInsurance(@RequestBody ComparisonRequest comparisonRequest) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_PREMIUM,
                comparisonService.createComparisonAndGetInsurance(comparisonRequest));
    }


    /*
     * admin
     */
    @GetMapping("/admin/comparison")
    @Operation(summary = "견적 요청 리스트 조회 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 견적 요청 정보를 조회")
    public ApiResponse getPetInfos(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "startDate", required = false) String startDate,
            @RequestParam(name = "endDate", required = false) String endDate,
            @RequestParam(name = "status", required = false, defaultValue = "PENDING") ComparisonStatus status
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_COMPARISON_TABLE, comparisonService.getComparisons(startDate, endDate, page, status));
    }


    @GetMapping("/admin/comparison/downloads")
    @Operation(summary = "견적 요청 리스트 엑셀 다운로드 API", description = "시작 날짜, 마지막 날짜, 상태값을 Parameter으로 받아 특정 기간 동안 생성된 견적 요청 정보를 가진 엑셀을 다운로드")
    public void downloadPetInfos(HttpServletResponse servletResponse,
                                 @RequestParam(name = "startDate") String startDate,
                                 @RequestParam(name = "endDate") String endDate,
                                 @RequestParam(name = "status", required = false, defaultValue = "PENDING") ComparisonStatus status) {
        comparisonService.downloadComparisons(servletResponse, startDate, endDate, status);
    }


    @GetMapping("/admin/comparison/search")
    @Operation(summary = "견적서 검색 API", description = "전화번호와 펫 이름으로 견적서 검색")
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


    @GetMapping("/comparison")
    @Operation(summary = "견적 요청 화면을 리다이렉트하는 API", description = "사용자의 기본 정보를 파라미터로 넘긴 후, 견적서 생성 화면으로 리다이렉트한다")
    public ResponseEntity<Void> getComparisonView(
            @RequestParam(value = "petName") String petName,
            @RequestParam(value = "petType") String petType,
            @RequestParam(value = "petSpecies") String petSpecies,
            @RequestParam(value = "petAge") String petAge,
            @RequestParam(value = "phoneNumber") String phoneNumber,
            @RequestParam(value = "referSite", required = false) String referSite,
            @RequestParam(value = "referUserId", required = false) String referUserId,
            HttpServletResponse response
    ) throws IOException {

        Map<String, String> params = new HashMap<>();
        params.put("petName", String.valueOf(petName));
        params.put("petType", String.valueOf(petType));
        params.put("petSpecies", String.valueOf(petSpecies));
        params.put("petAge", String.valueOf(petAge));
        params.put("phoneNumber", String.valueOf(phoneNumber));
        params.put("referSite", String.valueOf(referSite));
        params.put("referUserId", String.valueOf(referUserId));

        // redirect할 url 생성
        String redirectUrl = comparisonService.makeRedirectUrl(params);

        response.sendRedirect(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/comparison/form")
    @Operation(summary = "견적 요청 화면을 조회하는 API", description = "사용자의 기본 정보를 파라미터로 넘긴 후, 견적서 생성 화면을 조회한다")
    public ApiResponse<?> getComparisonForm(
            @RequestParam(value = "petName") String petName,
            @RequestParam(value = "petType") String petType,
            @RequestParam(value = "petSpecies") String petSpecies,
            @RequestParam(value = "petAge") String petAge,
            @RequestParam(value = "phoneNumber") String phoneNumber,
            @RequestParam(value = "referSite", required = false) String referSite,
            @RequestParam(value = "referUserId", required = false) String referUserId
    ){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_COMPARISON_VIEW);
    }

}
