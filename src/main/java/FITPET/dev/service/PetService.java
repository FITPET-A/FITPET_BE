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

    /*
     * 파라미터로 들어오는 문자열을 포함하는 상세 품종 명을 가진 Pet 객체 리스트를 반환
     * @param petTypeStr
     * @param detailType
     * @return
     */
    public PetResponse.PetSpeciesListDto getPetSpeciesList(String petTypeStr, String petSpecies){
        // 품종 조회
        PetType petType = PetType.getPetType(petTypeStr);

        // petSpecies 문자열을 상세 품종 필드에 포함하는 Pet 객체들을 List로 반환
        List<Pet> petList = petRepository.findPetListContainingDetailType(petType, petSpecies);

        return PetConverter.toPetSpeciesListDto(petList);
    }
}
