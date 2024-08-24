package FITPET.dev.converter;

import FITPET.dev.common.enums.*;
import FITPET.dev.entity.Insurance;

public class InsuranceConverter {
    public static Insurance toDogInsurance(Company company, int age, String dogBreedRank,
                                 RenewalCycle renewalCycle, CoverageRatio coverageRatio,
                                 Deductible deductible, Compensation compensation, int premium){
        return Insurance.builder()
                .company(company)
                .petType(PetType.DOG)
                .age(age)
                .dogBreedRank(dogBreedRank)
                .renewalCycle(renewalCycle)
                .coverageRatio(coverageRatio)
                .deductible(deductible)
                .compensation(compensation)
                .premium(premium)
                .build();
    }

    public static Insurance toCatInsurance(Company company, int age,
                                           RenewalCycle renewalCycle, CoverageRatio coverageRatio,
                                           Deductible deductible, Compensation compensation, int premium){
        return Insurance.builder()
                .company(company)
                .petType(PetType.CAT)
                .age(age)
                .dogBreedRank(null)
                .renewalCycle(renewalCycle)
                .coverageRatio(coverageRatio)
                .deductible(deductible)
                .compensation(compensation)
                .premium(premium)
                .build();
    }
}
