package FITPET.dev.converter;

import FITPET.dev.common.enums.*;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Insurance;

import java.util.List;

public class InsuranceConverter {
    public static Insurance toInsurance(Company company, PetType petType, int age, String dogBreedRank,
                                           RenewalCycle renewalCycle, CoverageRatio coverageRatio,
                                           Deductible deductible, Compensation compensation, int premium){
        return Insurance.builder()
                .company(company)
                .petType(petType)
                .age(age)
                .dogBreedRank(dogBreedRank)
                .renewalCycle(renewalCycle)
                .coverageRatio(coverageRatio)
                .deductible(deductible)
                .compensation(compensation)
                .premium(premium)
                .build();
    }

    public static InsuranceResponse.InsuranceDto toInsuranceDto(Insurance insurance){
        return InsuranceResponse.InsuranceDto.builder()
                .company(insurance.getCompany().getLabel())
                .premium(insurance.getPremium())
                .build();
    }

    public static InsuranceResponse.InsuranceListDto toInsuranceListDto(List<Insurance> insuranceList){
        List<InsuranceResponse.InsuranceDto> insuranceDtoList = insuranceList.stream()
                .map(InsuranceConverter::toInsuranceDto)
                .toList();

        return InsuranceResponse.InsuranceListDto.builder()
                .insuranceDtoList(insuranceDtoList)
                .build();
    }

    public static InsuranceResponse.InsuranceDetailDto toInsuranceDetailDto(Insurance insurance) {
        return InsuranceResponse.InsuranceDetailDto.builder()
                .petType(insurance.getPetType())
                .age(insurance.getAge())
                .dogBreedRank(insurance.getDogBreedRank())
                .renewalCycle(insurance.getRenewalCycle().getLabel())
                .coverageRatio(insurance.getCoverageRatio().getLabel())
                .deductible(insurance.getDeductible().getLabel())
                .compensation(insurance.getCompensation().getLabel())
                .premium(insurance.getPremium())
                .build();
    }

    public static InsuranceResponse.InsuranceDetailListDto toInsuranceDetailListDto(List<Insurance> insuranceList){
        List<InsuranceResponse.InsuranceDetailDto> insuranceDetailDtoList = insuranceList.stream()
                .map(InsuranceConverter::toInsuranceDetailDto)
                .toList();

        return InsuranceResponse.InsuranceDetailListDto.builder()
                .insuranceDetailDtoList(insuranceDetailDtoList)
                .build();
    }

}
