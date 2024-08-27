package FITPET.dev.converter;

import FITPET.dev.common.enums.Status;
import FITPET.dev.dto.request.PetInfoRequest;
import FITPET.dev.dto.response.PetInfoResponse;
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
                .status(Status.PENDING)
                .build();
    }

    public static PetInfoResponse.PetInfoExcelDto toPetInfoExcelDto(PetInfo petInfo) {
        Pet pet = petInfo.getPet();

        return PetInfoResponse.PetInfoExcelDto.builder()
                .status(petInfo.getStatus())
                .petInfoId(petInfo.getPetInfoId())
                .name(petInfo.getName())
                .age(petInfo.getAge())
                .phoneNum(petInfo.getPhoneNum())
                .createdAt(petInfo.getCreatedAt())
                .petType(pet.getPetType().toString())
                .detailType(pet.getDetailType())
                .build();
    }

}
