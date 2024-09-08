package FITPET.dev.converter;

import FITPET.dev.common.enums.*;
import FITPET.dev.dto.request.InsuranceRequest;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Insurance;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

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
        int discountedPremium = getDiscountedPremium(insurance.getCompany(), insurance.getPremium());

        return InsuranceResponse.InsuranceDto.builder()
                .insuranceId(insurance.getInsuranceId())
                .company(insurance.getCompany().getLabel())
                .premium(insurance.getPremium())
                .discountedPremium(discountedPremium)
                .build();
    }

    private static int getDiscountedPremium(Company company, int premium){
        // 회사별 할인율을 매핑
        Map<Company, Double> discountRates = Map.of(
                Company.SAMSUNG, 0.95,
                Company.DB, 0.98,
                Company.KB, 0.98,
                Company.HYUNDAE, 0.95,
                Company.MERITZ, 0.98
        );

        // 해당 회사의 할인률을 적용하고 외의 경우에는 원금 반환
        return (int) (premium * discountRates.getOrDefault(company, 1.0));
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
                .insuranceId(insurance.getInsuranceId())
                .petType(insurance.getPetType())
                .company(insurance.getCompany().getLabel())
                .age(insurance.getAge())
                .dogBreedRank(insurance.getDogBreedRank())
                .renewalCycle(insurance.getRenewalCycle().getLabel())
                .coverageRatio(insurance.getCoverageRatio().getLabel())
                .deductible(insurance.getDeductible().getLabel())
                .compensation(insurance.getCompensation().getLabel())
                .premium(insurance.getPremium())
                .build();
    }

    public static InsuranceResponse.InsuranceDetailPageDto toInsuranceDetailPageDto(Page<Insurance> page){
        List<InsuranceResponse.InsuranceDetailDto> insuranceDetailDtoList = page.getContent().stream()
                .map(InsuranceConverter::toInsuranceDetailDto)
                .toList();

        return InsuranceResponse.InsuranceDetailPageDto.builder()
                .content(insuranceDetailDtoList)
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .numberOfElement(page.getNumberOfElements())
                .totalElements((int) page.getTotalElements())                .totalPage(page.getTotalPages())
                .build();
    }

    public static InsuranceResponse.InsuranceExcelDto toInsuranceExcelDto(Insurance insurance){
        return InsuranceResponse.InsuranceExcelDto.builder()
                .company(insurance.getCompany().getLabel())
                .insuranceId(insurance.getInsuranceId())
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

    public static Insurance RequestToInsurance(InsuranceRequest request) {
        return Insurance.builder()
                .company(Company.getCompany(request.getCompany()))
                .petType(PetType.getPetType(request.getPetType()))
                .age(request.getAge())
                .dogBreedRank(request.getDogBreedRank())
                .renewalCycle(RenewalCycle.getRenewalCycle(request.getRenewalCycle()))
                .coverageRatio(CoverageRatio.getCoverageRatio(request.getCoverageRatio()))
                .deductible(Deductible.getDeductible(request.getDeductible()))
                .compensation(Compensation.getCompensation(request.getCompensation()))
                .premium(request.getPremium())
                .build();
    }

    public static InsuranceResponse.AllInsuranceListDto toAllInsuranceListDto(List<InsuranceResponse.InsuranceDto> seventyInsuranceList,
                                                                              List<InsuranceResponse.InsuranceDto> eightyInsuranceList,
                                                                              List<InsuranceResponse.InsuranceDto> ninetyInsuranceList){
        return InsuranceResponse.AllInsuranceListDto.builder()
                .seventyinsuranceDtoList(seventyInsuranceList)
                .eightyinsuranceDtoList(eightyInsuranceList)
                .ninetyinsuranceDtoList(ninetyInsuranceList)
                .build();
    }



}
