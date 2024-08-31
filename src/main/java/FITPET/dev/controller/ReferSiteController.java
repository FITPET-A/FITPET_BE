package FITPET.dev.controller;

import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.dto.request.ReferSiteRequest;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.ReferSiteResponse;
import FITPET.dev.entity.Insurance;
import FITPET.dev.service.ReferSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유입 채널 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class ReferSiteController {

    private final ReferSiteService referSiteService;

    @PostMapping("/refersite")
    @Operation(summary = "유입 채널 추가 API", description = "유입 채널을 새로 추가하는 API")
    public ApiResponse postRefSite(
            @RequestBody ReferSiteRequest.ReferSiteDto refSiteDto
    ) {
        ReferSiteResponse.ReferSiteDto responseDto = referSiteService.postReferSite(refSiteDto);
        return ApiResponse.SuccessResponse(SuccessStatus.POST_REFERSITE, responseDto);
    }


    @PatchMapping("/refersite/{referSiteId}")
    @Operation(summary = "유입 채널 수정 API", description = "특정 유입 채널을 수정하는 API")
    public ApiResponse updateRefSite(
            @PathVariable Long referSiteId,
            @RequestBody ReferSiteRequest.ReferSiteDto refSiteDto
    ) {
        ReferSiteResponse.ReferSiteDto responseDto = referSiteService.patchReferSite(referSiteId, refSiteDto);
        return ApiResponse.SuccessResponse(SuccessStatus.UPDATE_REFERSITE, responseDto);
    }


    @GetMapping("/refersite")
    @Operation(summary = "유입 채널 조회 API", description = "유입 채널을 페이지네이션으로 조회하는 API")
    public ApiResponse getReferSite(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_REFERSITE, referSiteService.getReferSite(page));
    }

    @DeleteMapping("/refersite/{referSiteId}")
    @Operation(summary = "유입 채널 삭제 API", description = "유입 채널을 삭제하는 API")
    public ApiResponse deleteReferSite(
            @PathVariable Long referSiteId
    ) {
        referSiteService.deleteReferSite(referSiteId);
        return ApiResponse.SuccessResponse(SuccessStatus.DELETE_REFERSITE);
    }



}
