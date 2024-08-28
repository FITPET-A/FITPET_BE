package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.service.InsuranceService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/insurance")
public class InsuranceController {
    private final InsuranceService insuranceService;

    @GetMapping("")
    public ApiResponse getInsurancePremium(@RequestParam(name = "detailType") String detailType,
                                           @RequestParam(name = "age", defaultValue = "0") int age,
                                           @RequestParam(name = "renewalCycle", defaultValue = "3년", required = false) String renewalCycle,
                                           @RequestParam(name = "coverageRatio", defaultValue = "70", required = false) String coverageRatio,
                                           @RequestParam(name = "deductible", defaultValue = "1만원", required = false) String deductible,
                                           @RequestParam(name = "compensation", defaultValue = "15만", required = false) String compensation
                                           ){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_PREMIUM,
                insuranceService.getInsurancePremium(detailType, age, renewalCycle, coverageRatio, deductible, compensation));
    }
}
