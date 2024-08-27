package FITPET.dev.converter;

import FITPET.dev.dto.request.PetInfoRequest;
import FITPET.dev.dto.response.PetInfoResponse;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PetInfoConverter {

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static PetInfo toPetInfo(PetInfoRequest request, Pet pet) {
        return PetInfo.builder()
                .name(request.getName())
                .age(request.getAge())
                .phoneNum(request.getPhoneNum())
                .pet(pet)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static PetInfoResponse.PetInfoExcelDto toPetInfoExcelDto(PetInfo petInfo) {
        Pet pet = petInfo.getPet();

        return PetInfoResponse.PetInfoExcelDto.builder()
                .petInfoId(petInfo.getPetInfoId())
                .name(petInfo.getName())
                .age(petInfo.getAge())
                .phoneNum(petInfo.getPhoneNum())
                .createdAt(formatDateTime(petInfo.getCreatedAt()))
                .petType(pet.getPetType().toString())
                .detailType(pet.getDetailType())
                .build();
    }



}
