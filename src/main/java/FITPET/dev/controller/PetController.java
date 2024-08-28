package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.service.PetService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/pet")
public class PetController {
    private final PetService petService;

    @GetMapping("")
    public ApiResponse getDetailTypeList(@RequestParam(name = "petType") String petType,
                                         @RequestParam(name = "detailType") String detailType){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_DETAILTYPE_LIST, petService.getPetDetailTypeList(petType, detailType));
    }
}
