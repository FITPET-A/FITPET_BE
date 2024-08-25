package FITPET.dev.converter;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.entity.Pet;

public class PetConverter {
    public static Pet toPet(PetType petType, String detailType){
        return Pet.builder()
                .petType(petType)
                .detailType(detailType)
                .build();
    }
}
