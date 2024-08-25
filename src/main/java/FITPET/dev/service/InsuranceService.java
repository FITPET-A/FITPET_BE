package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.enums.*;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.CompanyDogBreed;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.Pet;
import FITPET.dev.repository.InsuranceRepository;
import FITPET.dev.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final PetRepository petRepository;

    // 회사별 월 보험료 조회
    public InsuranceResponse.InsuranceListDto getInsurancePremium(String detailType, int age, String renewalCycle,
                                                                  String coverageRatio, String deductible, String compensation){
        // 품종 분류
        Pet pet = findPetByDetailType(detailType);

        // 1차 insurance list 조회
        List<Insurance> insuranceList = findInsurance(pet, age, renewalCycle, coverageRatio, deductible, compensation);
        if (pet.getPetType() == PetType.CAT)
            return InsuranceConverter.toInsuranceListDto(insuranceList);

        // dogBreedRank 분류 및 필터링
        List<CompanyDogBreed> companyDogBreedList = pet.getCompanyDogBreedList();
        List<Insurance> dogBreedInsuranceList = insuranceList.stream()
                .filter(insurance -> companyDogBreedList.stream()
                        .anyMatch(companyDogBreed ->
                                insurance.getCompany() == companyDogBreed.getCompany() &&
                                        insurance.getDogBreedRank().equals(companyDogBreed.getDogBreedRank())))
                .toList();

        return InsuranceConverter.toInsuranceListDto(dogBreedInsuranceList);
    }

    private Pet findPetByDetailType(String detailType){
        return petRepository.findByDetailType(detailType)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PET));
    }

    private List<Insurance> findInsurance(Pet pet, int age, String renewalCycle, String coverageRatio, String deductible, String compensation){
        return insuranceRepository.findInsuranceList(
                pet.getPetType(), age, RenewalCycle.getRenewalCycle(renewalCycle),
                CoverageRatio.getCoverageRatio(coverageRatio), Deductible.getDeductible(deductible),
                Compensation.getCompensation(compensation));
    }
}
