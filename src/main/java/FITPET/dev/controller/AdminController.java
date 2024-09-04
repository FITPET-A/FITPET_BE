package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.request.AdminRequest;
import FITPET.dev.service.AdminService;
import FITPET.dev.service.InitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "관리자 페이지 API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final InitService initService;
    private final AdminService adminService;

    // excel file DB에 저장
    @GetMapping("/init")
    public ApiResponse initDatabase() {
        return ApiResponse.SuccessResponse(SuccessStatus.SUCCESS);
    }

    @Operation(summary = "[TEMP] 관리자 계정 회원가입", description = "id, pwd, nickname 정보를 받아 회원가입 후 토큰 반환")
    @PostMapping("/sign-up")
    public ApiResponse signUp(@RequestBody AdminRequest.SignUpDto signUpDto) {
        return ApiResponse.SuccessResponse(SuccessStatus.SUCCESS_SIGN_UP, adminService.signUp(signUpDto));
    }

    @Operation(summary = "관리자 계정 로그인", description = "id, pwd 정보를 받아 로그인 후 토큰 반환")
    @PostMapping("/sign-in")
    public ApiResponse signIn(@RequestBody AdminRequest.SignInDto signInDto) {
        return ApiResponse.SuccessResponse(SuccessStatus.SUCCESS_SIGN_IN, adminService.signIn(signInDto));
    }

}
