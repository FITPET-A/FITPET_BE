package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.enums.PetType;
import FITPET.dev.common.exception.GeneralException;
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
        PetType petType = PetType.getPetType(petTypeStr);

        if (petType == null)
            throw new GeneralException(ErrorStatus.INVALID_PET_TYPE);

        List<Pet> petList = petRepository.findByPetTypeAndDetailTypeContaining(petType, detailType);
        return PetConverter.toPetDetailTypeListDto(petList);
    }
}
