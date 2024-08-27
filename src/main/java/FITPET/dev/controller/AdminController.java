package FITPET.dev.controller;

import FITPET.dev.common.basecode.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.service.AdminService;
import FITPET.dev.service.InitService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final InitService initService;
    private final AdminService adminService;

    // excel file DB에 저장
    @GetMapping("/init")
    public ApiResponse initDatabase(){
//        initService.initDatabase();
        return ApiResponse.SuccessResponse(SuccessStatus.SUCCESS);
    }

    @GetMapping("/insurance")
    public ApiResponse getInsurances(
            @RequestParam(name = "company", required = false, defaultValue = "all") String company,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_INSURANCE_TABLE, adminService.getInsurances(page, company));
    }

    @GetMapping("/insurance/downloads")
    public void downloadInsurances(
            HttpServletResponse servletResponse,
            @RequestParam(name = "company", required = false, defaultValue = "all") String company
    ){
        adminService.downloadInsurances(servletResponse, company);
    }

    @GetMapping("/petinfo/downloads")
    public void downloadPetInfos(HttpServletResponse servletResponse){
        adminService.downloadPetInfos(servletResponse);
    }

    @GetMapping("/petinfo")
    public ApiResponse getPetInfos(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_PETINFO_TABLE, adminService.getPetInfos(page));
    }

}
