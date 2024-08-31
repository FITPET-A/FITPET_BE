package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.service.InitService;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "관리자 페이지 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final InitService initService;

    // excel file DB에 저장
    @GetMapping("/init")
    public ApiResponse initDatabase() {
        return ApiResponse.SuccessResponse(SuccessStatus.SUCCESS);
    }
}
