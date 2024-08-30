package FITPET.dev.converter;

import FITPET.dev.dto.response.ComparisonResponse;
import FITPET.dev.entity.Comparison;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ComparisonConverter {

    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static ComparisonResponse.ComparisonExcelDto toComparisonExcelDto(Comparison comparison) {
        PetInfo petInfo = comparison.getPetInfo();
        Pet pet = petInfo.getPet();

        return ComparisonResponse.ComparisonExcelDto.builder()
                .status(comparison.getStatus())
                .comparisonId(comparison.getComparisonId())
                .petName(petInfo.getName())
                .petAge(petInfo.getAge())
                .phoneNumber(petInfo.getPhoneNum())
                .createdAt(formatDateTime(petInfo.getCreatedAt()))
                .petType(pet.getPetType().toString())
                .petSpecies(pet.getPetSpecies())
                .comment(comparison.getComment())
                .referSite(comparison.getReferSite())
                .referSiteUserId(comparison.getReferSiteUserId())
                .build();
    }

    public static ComparisonResponse.ComparisonDto toComparisonDto(Comparison comparison) {
        PetInfo petInfo = comparison.getPetInfo();
        Pet pet = petInfo.getPet();

        return ComparisonResponse.ComparisonDto.builder()
                .comparisonId(comparison.getComparisonId())
                .petName(petInfo.getName())
                .petAge(petInfo.getAge())
                .phoneNumber(petInfo.getPhoneNum())
                .createdAt(formatDateTime(petInfo.getCreatedAt()))
                .petType(pet.getPetType().toString())
                .petSpecies(pet.getPetSpecies())
                .comment(comparison.getComment())
                .status(comparison.getStatus().getLabel())
                .build();
    }

    public static ComparisonResponse.ComparisonPageDto toComparisonPageDto(
            Page<Comparison> comparisonPage) {

        List<ComparisonResponse.ComparisonDto> comparisonDtoList = comparisonPage.getContent().stream()
                .map(ComparisonConverter::toComparisonDto)
                .toList();

        return ComparisonResponse.ComparisonPageDto.builder()
                .content(comparisonDtoList)
                .currentPage(comparisonPage.getNumber())
                .pageSize(comparisonPage.getSize())
                .totalNumber((int) comparisonPage.getTotalElements())
                .totalPage(comparisonPage.getTotalPages())
                .build();
    }

}
