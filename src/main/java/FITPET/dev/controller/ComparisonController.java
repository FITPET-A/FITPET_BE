package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.ComparisonRequest;
import FITPET.dev.service.ComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Tag(name = "견적서 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/comparison")
public class ComparisonController {

    private final ComparisonService comparisonService;

    @PostMapping("")
    @Operation(summary = "견적서 생성 API", description = "견적서를 생성하고 기본 보험료를 조회")
    public ApiResponse<?> createComparisonAndGetInsurance(@RequestBody ComparisonRequest comparisonRequest) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_PREMIUM,
                comparisonService.createComparisonAndGetInsurance(comparisonRequest));
    }

    @GetMapping("")
    @Operation(summary = "견적 요청 화면을 조회하는 API", description = "사용자의 기본 정보를 파라미터로 넘긴 후, 견적서 생성 화면을 조회한다")
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

        // redirect할 url 생성
        String redirectUrl = "/api/v1/comparison/form";

        redirectUrl += "?";
        redirectUrl += "petName=" + petName + "&";
        redirectUrl += "petType=" + petType + "&";
        redirectUrl += "petSpecies=" + petSpecies + "&";
        redirectUrl += "petAge=" + petAge + "&";
        redirectUrl += "phoneNumber=" + phoneNumber + "&";
        if (referSite != null || referUserId != null) {
            if (referSite != null) {
                redirectUrl += "referSite=" + referSite + "&";
            }
            if (referUserId != null) {
                redirectUrl += "referUserId=" + referUserId;
            }
        }

        response.sendRedirect(redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).build();
    }

    @GetMapping("/form")
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
