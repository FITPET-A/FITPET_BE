package FITPET.dev.controller;

import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.dto.request.ProposalRequest;
import FITPET.dev.dto.request.RefSiteRequest;
import FITPET.dev.dto.response.RefSiteResponse;
import FITPET.dev.service.ProposalService;
import FITPET.dev.service.RefSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.sql.Ref;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유입 채널 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class RefSiteController {

    private final RefSiteService refSiteService;

    @PostMapping("/refsite")
    @Operation(summary = "유입 채널 추가 API", description = "유입 채널을 새로 추가하는 API")
    public ApiResponse postRefSite(@RequestBody RefSiteRequest.RefSiteDto refSiteDto) {
        RefSiteResponse.RefSiteDto responseDto = refSiteService.postRefSite(refSiteDto);
        return ApiResponse.SuccessResponse(SuccessStatus.POST_REFSITE, responseDto);
    }

}
