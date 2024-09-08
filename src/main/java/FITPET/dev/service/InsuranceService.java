package FITPET.dev.service;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.enums.*;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.utils.ExcelUtils;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.converter.InsuranceHistoryConverter;
import FITPET.dev.dto.request.InsuranceRequest;
import FITPET.dev.dto.response.InsuranceHistoryResponse;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.CompanyDogBreed;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.InsuranceHistory;
import FITPET.dev.entity.Pet;
import FITPET.dev.repository.InsuranceHistoryRepository;
import FITPET.dev.repository.InsuranceRepository;
import FITPET.dev.repository.PetRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static FITPET.dev.common.enums.Compensation.getCompensation;
import static FITPET.dev.common.enums.CoverageRatio.getCoverageRatio;
import static FITPET.dev.common.enums.Deductible.getDeductible;
import static FITPET.dev.common.enums.RenewalCycle.getRenewalCycle;
import static FITPET.dev.service.ComparisonService.MAX_AGE;

@Service
@RequiredArgsConstructor
public class InsuranceService {
    private final InsuranceRepository insuranceRepository;
    private final PetRepository petRepository;
    private final InsuranceHistoryRepository insuranceHistoryRepository;
    private final ExcelUtils excelUtils;


    /*
     * 회사별 월 보험료 리스트를 조회
     * @param detailType
     * @param age
     * @param renewalCycle
     * @param coverageRatio
     * @param deductible
     * @param compensation
     * @return
     */
    public InsuranceResponse.InsuranceListDto getInsurancePremium(
            String petSpecies, int age, String renewalCycle, String coverageRatio, String deductible, String compensation){

        // 품종 분류
        Pet pet = findPetByPetSpecies(petSpecies);

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


    /*
     * 필드 정보들을 만족하는 insurance 객체들을 찾아 리스트로 반환
     * @param pet
     * @param age
     * @param renewalCycle
     * @param coverageRatio
     * @param deductible
     * @param compensation
     * @return
     */
    private List<Insurance> findInsurancePremiumList(Pet pet, int age, String renewalCycle,
                                           String coverageRatio, String deductible, String compensation){

        return insuranceRepository.findInsuranceList(pet.getPetType(), age,
                getRenewalCycle(renewalCycle),
                getCoverageRatio(coverageRatio),
                getDeductible(deductible),
                getCompensation(compensation));
    }


    /*
     * 회사별, 품종별 보험 테이블 조회
     * @param page
     * @param companyStr
     * @param petType
     * @return
     */
    public InsuranceResponse.InsuranceDetailPageDto getInsurances(int page, String companyStr, String petType) {

        // 페이지 크기, 페이지 번호 정보를 Pageable 객체에 설정
        Pageable pageable = PageRequest.of(page, 20);

        // company 문자열, 품종에 따라 회사별 Insurance 정보를 페이징 객체로 반환
        Page<Insurance> insurancePage = getInsurancePageByCompanyAndPetType(companyStr, petType, pageable);

        // converter을 통해 pageDto로 타입 변경 후 리턴
        return InsuranceConverter.toInsuranceDetailPageDto(insurancePage);
    }


    /*
     * 회사별 보험 테이블 엑셀 다운로드
     * @param servletResponse
     * @param companyStr
     */
    public void downloadInsurances(HttpServletResponse servletResponse) {

        // 전체 보험 정보 List 조회
        List<Insurance> insuranceList = insuranceRepository.findAll();

        // excelDto로 타입 변경
        List<InsuranceResponse.InsuranceExcelDto> insuranceExcelDtoList = convertToInsuranceExcelDtoList(
                insuranceList);

        // excel 파일 다운로드
        excelUtils.downloadInsurances(servletResponse, insuranceExcelDtoList);
    }


    /*
     * 보험료 수정
     * @param insuracneId
     * @param premium
     */
    @Transactional
    public InsuranceResponse.InsuranceDetailDto updateInsurancePremium(Long insuranceId, int premium){
        Insurance insurance = findInsuranceById(insuranceId);

        int oldPremium = insurance.getPremium();
        insurance.updatepremium(premium);

        saveInsuranceHistory(insurance, oldPremium, premium);

        return InsuranceConverter.toInsuranceDetailDto(insurance);
    }


    /*
     * 보험료 히스토리 조회
     * @param insuranceId
     */
    public List<InsuranceHistoryResponse> getPremiumHistory(Long insuranceId) {
        List<InsuranceHistory> histories = insuranceHistoryRepository
                .findByInsurance_InsuranceIdOrderByCreatedAtDesc(insuranceId);
        return InsuranceHistoryConverter.toResponseList(histories);
    }


    /*
     * 보험 정보 추가
     */
    @Transactional
    public InsuranceResponse.InsuranceDetailDto addInsurance(InsuranceRequest request) {
        Insurance insurance = InsuranceConverter.RequestToInsurance(request);
        insuranceRepository.save(insurance);
        return InsuranceConverter.toInsuranceDetailDto(insurance);
    }


    /*
     * 보험 정보 삭제
     * @param insuranceId
     */
    @Transactional
    public void deleteInsurance(Long insuranceId) {
        Insurance insurance = findInsuranceById(insuranceId);
        if (insurance.getDeletedAt() != null) {
            throw new GeneralException(ErrorStatus.ALREADY_DELETED_INSURANCE);
        }
        insurance.setDeletedAt();
    }


    /*
     * 삭제된 보험 정보 조회
     */
    public InsuranceResponse.InsuranceDetailPageDto getDeletedInsurances(int page) {
        Pageable pageable = PageRequest.of(page, 20);
        Page<Insurance> insurancePage = insuranceRepository.findDeleted(pageable);
        return InsuranceConverter.toInsuranceDetailPageDto(insurancePage);
    }


    private Page<Insurance> getInsurancePageByCompanyAndPetType(String companyStr, String petTypeStr, Pageable pageable) {

        Company company = (companyStr.equals("all") ? null : Company.getCompany(companyStr));
        PetType petType = (petTypeStr.equals("all") ? null : PetType.getPetType(petTypeStr));

        if (company == null) {
            // 전체 insurance Page 반환
            return (petType == null) ?
                    insuranceRepository.findAll(pageable) :
                    insuranceRepository.findByPetType(petType, pageable);
        } else {
            // company 정보가 일치하는 insurance Page 반환
            return (petType == null) ?
                    insuranceRepository.findByCompany(company, pageable) :
                    insuranceRepository.findByCompanyAndPetType(company, petType, pageable);
        }
    }


    private List<InsuranceResponse.InsuranceExcelDto> convertToInsuranceExcelDtoList(
            List<Insurance> insuranceList) {
        return insuranceList.stream()
                .map(InsuranceConverter::toInsuranceExcelDto)
                .toList();
    }


    private void saveInsuranceHistory(Insurance insurance, int oldPremium, int newPremium) {
        // 보험 수정 이력 저장
        InsuranceHistory history = InsuranceHistoryConverter.createHistory(insurance, oldPremium, newPremium);
        insuranceHistoryRepository.save(history);
    }


    /*
     * 상세 품종명으로 Pet 객체를 찾아 반환
     * @param detailType
     * @return
     */
    private Pet findPetByPetSpecies(String petSpecies){
        return petRepository.findByPetSpecies(petSpecies)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PET));
    }


    private Insurance findInsuranceById(Long insuranceId){
        return insuranceRepository.findByInsuranceId(insuranceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_INSURANCE));
    }


    private void validateAge(int age) {
        if (age < 0) {
            throw new GeneralException(ErrorStatus.INVALID_AGE);
        } else if (age > MAX_AGE) {
            throw new GeneralException(ErrorStatus.INVALID_AGE_OVER_10);
        }
    }

}
