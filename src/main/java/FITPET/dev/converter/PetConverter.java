package FITPET.dev.converter;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.dto.response.PetResponse;
import FITPET.dev.entity.Pet;

import java.util.List;

public class PetConverter {
    public static Pet toPet(PetType petType, String detailType){
        return Pet.builder()
                .petType(petType)
                .detailType(detailType)
                .build();
    }

    public static PetResponse.PetDetailTypeListDto toPetDetailTypeListDto(List<Pet> petList){
        List<String> detailTypeList = petList.stream()
                .map(Pet::getDetailType)
                .toList();

        return PetResponse.PetDetailTypeListDto.builder()
                .detailTypeList(detailTypeList)
                .build();
    }
}
