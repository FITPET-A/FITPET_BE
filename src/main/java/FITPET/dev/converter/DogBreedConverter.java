package FITPET.dev.converter;

import FITPET.dev.common.enums.Company;
import FITPET.dev.entity.CompanyDogBreed;
import FITPET.dev.entity.DogBreedDetail;
import FITPET.dev.entity.Pet;

public class DogBreedConverter {
    public static DogBreedDetail toDogBreedDetail(Pet pet, boolean isFierceDog, String liabilityCap, String isMajorDogBreed){
        return DogBreedDetail.builder()
                .pet(pet)
                .isFierceDog(isFierceDog)
                .liabilityCap(liabilityCap)
                .isMajorDogBreed(isMajorDogBreed)
                .build();
    }

    public static CompanyDogBreed toCompanyDogBreed(Pet pet, Company company, String dogBreed, String dogBreedRank){
        return CompanyDogBreed.builder()
                .pet(pet)
                .company(company)
                .dogBreed(dogBreed)
                .dogBreedRank(dogBreedRank)
                .build();
    }
}
