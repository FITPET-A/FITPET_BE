package FITPET.dev.converter;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.dto.response.PetResponse;
import FITPET.dev.entity.Pet;

import java.util.List;

public class PetConverter {
    public static Pet toPet(PetType petType, String petSpecies){
        return Pet.builder()
                .petType(petType)
                .petSpecies(petSpecies)
                .build();
    }

    public static PetResponse.PetSpeciesListDto toPetSpeciesListDto(List<Pet> petList){
        List<String> petSpeciesList = petList.stream()
                .map(Pet::getPetSpecies)
                .toList();

        return PetResponse.PetSpeciesListDto.builder()
                .petSpeciesList(petSpeciesList)
                .build();
    }
}
