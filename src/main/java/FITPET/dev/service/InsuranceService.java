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

import static FITPET.dev.common.enums.Compensation.getCompensation;
import static FITPET.dev.common.enums.CoverageRatio.getCoverageRatio;
import static FITPET.dev.common.enums.Deductible.getDeductible;
import static FITPET.dev.common.enums.RenewalCycle.getRenewalCycle;
import static FITPET.dev.service.PetInfoService.MAX_AGE;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final PetRepository petRepository;

    // 회사별 월 보험료 조회
    public InsuranceResponse.InsuranceListDto getInsurancePremium(
            String detailType, int age, String renewalCycle, String coverageRatio, String deductible, String compensation){

        // 품종 분류
        Pet pet = findPetByDetailType(detailType);

        // validate age parameter
        validateAge(age);

        // 1차 insurance list 조회
        List<Insurance> insuranceList = findInsurancePremiumList(pet, age, renewalCycle, coverageRatio, deductible, compensation);
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

    private List<Insurance> findInsurancePremiumList(Pet pet, int age, String renewalCycle,
                                           String coverageRatio, String deductible, String compensation){

        return insuranceRepository.findInsuranceList(pet.getPetType(), age,
                getRenewalCycle(renewalCycle),
                getCoverageRatio(coverageRatio),
                getDeductible(deductible),
                getCompensation(compensation));
    }

    // 나이 유효성 검사
    private void validateAge(int age) {
        if (age < 0) {
            throw new GeneralException(ErrorStatus.INVALID_AGE);
        } else if (age > MAX_AGE) {
            throw new GeneralException(ErrorStatus.INVALID_AGE_OVER_10);
        }
    }

}
