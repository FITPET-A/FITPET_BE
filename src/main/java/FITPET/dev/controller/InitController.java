package FITPET.dev.controller;

import FITPET.dev.common.basecode.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.service.InitService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/init")
public class InitController {
    private final InitService initService;

    // excel file DB에 저장
    @GetMapping("")
    public ApiResponse initDatabase(){
        initService.initDatabase();
        return ApiResponse.SuccessResponse(SuccessStatus.SUCCESS);
    }

}
