package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.PetInfoConverter;
import FITPET.dev.dto.request.PetInfoRequest;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Pet;
import FITPET.dev.entity.PetInfo;
import FITPET.dev.repository.PetInfoRepository;
import FITPET.dev.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetInfoService {

    private final PetInfoRepository petInfoRepository;
    private final PetRepository petRepository;
    private final InsuranceService insuranceService;


    // PetInfo 생성 및 저장 후 보험료 조회
    public InsuranceResponse.InsuranceListDto createPetInfoAndGetInsurance(PetInfoRequest petInfoRequest) {
        Pet pet = findPetByDetailType(petInfoRequest.getDetailType());

        // PetInfo 생성하고 저장
        PetInfo petInfo = PetInfoConverter.toPetInfo(petInfoRequest, pet);
        petInfo = petInfoRepository.save(petInfo);

        // 보험료 조회
        return getInsurancePremiumsByPetInfo(petInfo);
    }

    // detail_type을 기반으로 Pet 조회
    private Pet findPetByDetailType(String detailType) {
        return petRepository.findByDetailType(detailType)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PET));
    }

    // default 값으로 보험료 조회
    public InsuranceResponse.InsuranceListDto getInsurancePremiumsByPetInfo(PetInfo petInfo) {

        String detailType = petInfo.getPet().getDetailType();
        int age = petInfo.getAge();
        String renewalCycle = "3년";
        String coverageRatio = "70%";
        String deductible = "1만원";
        String compensation = "15만";

        return insuranceService.getInsurancePremium(detailType, age, renewalCycle, coverageRatio, deductible, compensation);
    }


}
