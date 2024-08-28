package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.PetInfoRequest;
import FITPET.dev.service.PetInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "견적서 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/petinfo")
public class PetInfoController {

    private final PetInfoService petInfoService;

    @PostMapping("")
    @Operation(summary = "견적서 생성 API", description = "견적서를 생성하고 기본 보험료를 조회")
    public ApiResponse createPetInfoAndGetInsurance(@RequestBody PetInfoRequest petInfoRequest) {
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_PREMIUM,
                petInfoService.createPetInfoAndGetInsurance(petInfoRequest));
    }

}
