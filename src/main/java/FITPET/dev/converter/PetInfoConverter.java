package FITPET.dev.converter;

import FITPET.dev.dto.request.ComparisonRequest;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;

public class PetInfoConverter {

    public static PetInfo toPetInfo(ComparisonRequest request, Pet pet) {
        return PetInfo.builder()
                .name(request.getPetName())
                .age(request.getPetAge())
                .phoneNum(request.getPhoneNumber())
                .pet(pet)
                .build();
    }
}
