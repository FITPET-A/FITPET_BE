package FITPET.dev.converter;

import FITPET.dev.common.enums.Status;
import FITPET.dev.dto.request.PetInfoRequest;
import FITPET.dev.dto.response.PetInfoResponse;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Page;

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
                .status(Status.PENDING)
                .comment(request.getComment())
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
                .createdAt(formatDateTime(petInfo.getCreatedAt()))
                .petType(pet.getPetType().toString())
                .detailType(pet.getDetailType())
                .comment(petInfo.getComment())
                .build();
    }


    public static PetInfoResponse.PetInfoDetailDto toPetInfoDetailDto(PetInfo petInfo) {
        Pet pet = petInfo.getPet();

        return PetInfoResponse.PetInfoDetailDto.builder()
                .petInfoId(petInfo.getPetInfoId())
                .name(petInfo.getName())
                .age(petInfo.getAge())
                .phoneNum(petInfo.getPhoneNum())
                .createdAt(formatDateTime(petInfo.getCreatedAt()))
                .petType(pet.getPetType().toString())
                .detailType(pet.getDetailType())
                .comment(petInfo.getComment())
                .status(petInfo.getStatus().toString())
                .build();
    }

    public static PetInfoResponse.PetInfoDetailPageDto toPetInfoDetailPageDto(
            Page<PetInfo> petInfoPage) {

        List<PetInfoResponse.PetInfoDetailDto> petInfoDetailDtoList = petInfoPage.getContent().stream()
                .map(PetInfoConverter::toPetInfoDetailDto)
                .toList();

        return PetInfoResponse.PetInfoDetailPageDto.builder()
                .content(petInfoDetailDtoList)
                .currentPage(petInfoPage.getNumber())
                .pageSize(petInfoPage.getSize())
                .totalNumber((int) petInfoPage.getTotalElements())
                .totalPage(petInfoPage.getTotalPages())
                .build();
    }

}
