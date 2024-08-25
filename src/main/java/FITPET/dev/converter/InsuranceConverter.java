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

}
