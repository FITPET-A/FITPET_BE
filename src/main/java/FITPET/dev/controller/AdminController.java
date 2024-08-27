package FITPET.dev.controller;

import FITPET.dev.common.basecode.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.dto.response.PetInfoResponse;
import FITPET.dev.dto.response.PetInfoResponse.PetInfoExcelDto;
import FITPET.dev.service.AdminService;
import FITPET.dev.service.InitService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    //petinfo 조회
    @GetMapping("/petinfo")
    public ApiResponse getPetInfos(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size,
            @RequestParam(name = "sort", required = false, defaultValue = "createdAt") String sort,
            @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction
    ) {
        // Sort 생성
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Sort sortBy = Sort.by(sortDirection, sort);
        Pageable pageable = PageRequest.of(page, size, sortBy);

        Page<PetInfoExcelDto> petInfoPage = adminService.getPetInfos(pageable);
        return ApiResponse.SuccessResponse(SuccessStatus.GET_PETINFO_TABLE, petInfoPage);
    }

}
