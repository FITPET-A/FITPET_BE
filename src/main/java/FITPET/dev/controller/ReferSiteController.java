package FITPET.dev.controller;

import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.dto.request.ReferSiteRequest;
import FITPET.dev.dto.response.ReferSiteResponse;
import FITPET.dev.service.ReferSiteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유입 채널 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class ReferSiteController {

    private final ReferSiteService referSiteService;

    @PostMapping("/refsite")
    @Operation(summary = "유입 채널 추가 API", description = "유입 채널을 새로 추가하는 API")
    public ApiResponse postRefSite(@RequestBody ReferSiteRequest.ReferSiteDto refSiteDto) {
        ReferSiteResponse.ReferSiteDto responseDto = referSiteService.postReferSite(refSiteDto);
        return ApiResponse.SuccessResponse(SuccessStatus.POST_REFERSITE, responseDto);
    }

}
