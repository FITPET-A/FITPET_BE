package FITPET.dev.service;

import FITPET.dev.common.enums.PetType;
import FITPET.dev.converter.PetConverter;
import FITPET.dev.dto.response.PetResponse;
import FITPET.dev.entity.Pet;
import FITPET.dev.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    // 상세 품종 이름 조회
    public PetResponse.PetDetailTypeListDto getPetDetailTypeList(String petTypeStr, String detailType){
        // 품종 조회
        PetType petType = PetType.getPetType(petTypeStr);

        // detailType 문자열을 상세 품종 필드에 포함하는 Pet 객체들을 List로 반환
        List<Pet> petList = petRepository.findPetListContainingDetailType(petType, detailType);

        return PetConverter.toPetDetailTypeListDto(petList);
    }
}
