package FITPET.dev.controller;

import FITPET.dev.common.status.SuccessStatus;
import FITPET.dev.common.response.ApiResponse;
import FITPET.dev.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pet API")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/pet")
public class PetController {
    private final PetService petService;

    @GetMapping("")
    @Operation(summary = "상세 품종 명 자동 완성 API", description = "Parameter으로 들어오는 문자열을 포함하는 상세 품종 명을 가진 Pet 객체 리스트를 반환")
    public ApiResponse getPetSpeciesList(@RequestParam(name = "petType") String petType,
                                         @RequestParam(name = "petSpecies", required = false) String petSpecies){
        return ApiResponse.SuccessResponse(SuccessStatus.GET_DETAILTYPE_LIST, petService.getPetSpeciesList(petType, petSpecies));
    }
}
