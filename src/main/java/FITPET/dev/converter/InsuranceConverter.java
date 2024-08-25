package FITPET.dev.converter;

import FITPET.dev.common.enums.*;
import FITPET.dev.entity.Insurance;

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

}
