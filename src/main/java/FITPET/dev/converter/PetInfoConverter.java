package FITPET.dev.converter;

import FITPET.dev.dto.request.PetInfoRequest;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import java.time.LocalDateTime;

public class PetInfoConverter {
    public static PetInfo toPetInfo(PetInfoRequest request, Pet pet) {
        return PetInfo.builder()
                .name(request.getName())
                .age(request.getAge())
                .phoneNum(request.getPhoneNum())
                .pet(pet)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
