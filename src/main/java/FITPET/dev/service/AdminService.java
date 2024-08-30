package FITPET.dev.service;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.enums.PetType;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.enums.Company;
import FITPET.dev.common.enums.ComparisonStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.utils.ExcelUtils;
import FITPET.dev.converter.*;
import FITPET.dev.dto.request.InsuranceRequest;
import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.dto.response.InsuranceHistoryResponse;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.ComparisonResponse;
import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.*;
import FITPET.dev.repository.*;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final InsuranceRepository insuranceRepository;
    private final PetInfoRepository petInfoRepository;
    private final InquiryRepository inquiryRepository;
    private final ProposalRepository proposalRepository;
    private final InsuranceHistoryRepository insuranceHistoryRepository;
    private final ComparisonRepository comparisonRepository;
    private final ExcelUtils excelUtils;

    // 최소, 최대 조회 기간
    private final LocalDateTime minDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
    private final LocalDateTime maxDateTime = LocalDateTime.of(3999, 12, 31, 23, 59, 59);


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
     * 견적 요청 내역 조회 (최신순)
     * @param startDate
     * @param endDate
     * @param page
     * @param petInfoStatus
     * @return
     */
    public ComparisonResponse.ComparisonPageDto getComparisons(String startDate, String endDate, int page, ComparisonStatus comparisonStatus) {

        // 날짜 형식 변경
        LocalDateTime start = parseDate(startDate, " 00:00:00");
        LocalDateTime end = parseDate(endDate, " 23:59:59");

        // 견적 요청 리스트를 페이지 단위로 조회
        Pageable pageable = PageRequest.of(page, 20);
        Page<Comparison> comparisonPage = comparisonRepository.findByCreatedAtBetweenAndStatus(start, end, comparisonStatus, pageable);

        return ComparisonConverter.toComparisonPageDto(comparisonPage);
    }


    /*
     * 견적 요청 내역 엑셀 다운로드
     * @param servletResponse
     */
    public void downloadComparisons(HttpServletResponse servletResponse,
                                 String startDate, String endDate, ComparisonStatus comparisonStatus) {
        // 날짜 형식 변경
        LocalDateTime start = (startDate != null) ? parseDate(startDate, " 00:00:00") : minDateTime;
        LocalDateTime end = (endDate != null) ? parseDate(endDate, " 23:59:59") : maxDateTime;

        // 견적서 요청 리스트 조회
        List<PetInfo> petInfoList = petInfoRepository.findByCreatedAtBetweenAndStatus(start, end, comparisonStatus);

        // excelDto로 타입 변경
        List<ComparisonResponse.ComparisonExcelDto> comparisonExcelDtoList = convertToComparisonExcelDtoList(petInfoList);

        // excel 파일 다운로드
        excelUtils.downloadPetInfos(servletResponse, comparisonExcelDtoList);
    }


    /*
     * 견적 요청 상태 변경
     * @param petInfoId
     * @param petInfoStatus
     * @return
     */
    @Transactional
    public ComparisonResponse.ComparisonDto patchComparisonStatus(Long comparisonId, ComparisonStatus comparisonStatus){

        // 견적서 단일 조회
        Comparison comparison = findComparisonById(comparisonId);

        // validate status
        ComparisonStatus currentComparisonStatus = comparison.getStatus();
        if (currentComparisonStatus.getIndex() > comparisonStatus.getIndex())
            throw new GeneralException(ErrorStatus.INVALID_PATCH_PERIOR_STATUS);

        // patch status
        comparison.updateStatus(comparisonStatus);
        return ComparisonConverter.toComparisonDto(comparison);
    }

    private Comparison findComparisonById(Long comparisonId) {

    }


    /*
     * 1:1 문의 내역 조회
     * @param startDate
     * @param endDate
     * @param inquiryStatus
     * @return
     */
    public InquiryResponse.InquiryListDto getInquiries(String startDate, String endDate, InquiryStatus inquiryStatus){

        // 날짜 형식 변경
        LocalDateTime start = (startDate != null) ? parseDate(startDate, " 00:00:00") : minDateTime;
        LocalDateTime end = (endDate != null) ? parseDate(endDate, " 23:59:59") : maxDateTime;

        // 문의 내역 조회
        List<Inquiry> inquiryList = getInquiryListByStatusBetweenDate(start, end, inquiryStatus);

        return InquiryConverter.toInquiryListDto(inquiryList);
    }


    /*
     * 1:1 문의 상태 변경
     * @param inquiryId
     * @param inquiryStatus
     * @return
     */
    @Transactional
    public InquiryResponse.InquiryDto patchInquiryStatus(Long inquiryId, InquiryStatus inquiryStatus) {

        // 1:1 문의 단일 조회
        Inquiry inquiry = findInquiryById(inquiryId);

        // validate status
        InquiryStatus currentInquiryStatus = inquiry.getStatus();
        if (currentInquiryStatus.getIndex() > inquiryStatus.getIndex())
            throw new GeneralException(ErrorStatus.INVALID_PATCH_PERIOR_STATUS);

        // patch status
        inquiry.updateStatus(inquiryStatus);
        return InquiryConverter.toInquiryDto(inquiry);
    }


    /*
     * 제휴문의 내역 전체 조회
     * @return
     */
    public ProposalResponse.ProposalListDto getProposals(){

        // 전체 제휴문의 내역 조회
        List<Proposal> proposalList = proposalRepository.findAllByOrderByCreatedAtDesc();

        return ProposalConverter.toProposalListDto(proposalList);
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



    private List<Inquiry> getInquiryListByStatusBetweenDate(LocalDateTime start, LocalDateTime end, InquiryStatus inquiryStatus){

        if (inquiryStatus == null){
            // 특정 기간동안 생성된 inquiry List 최신순 정렬 후 반환
            return inquiryRepository.findByCreatedAtBetween(start, end);
        } else {
            // 특정 기간동안 생성된 status 정보가 일치하는 inquiry List 최신순 정렬 후 반환
            return inquiryRepository.findByCreatedAtBetweenAndStatus(start, end, inquiryStatus);
        }
    }


    private List<InsuranceResponse.InsuranceExcelDto> convertToInsuranceExcelDtoList(
            List<Insurance> insuranceList) {
        return insuranceList.stream()
                .map(InsuranceConverter::toInsuranceExcelDto)
                .toList();
    }


    private List<ComparisonResponse.ComparisonExcelDto> convertToComparisonExcelDtoList(
            List<Comparison> comparisonList) {
        return comparisonList.stream()
                .map(ComparisonConverter::toComparisonExcelDto)
                .toList();
    }


    private LocalDateTime parseDate(String date, String timeSuffix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            return LocalDateTime.parse(date + timeSuffix, formatter);
        } catch (DateTimeParseException e) {
            throw new GeneralException(ErrorStatus.INVALID_DATE_FORMAT);
        }
    }


    private PetInfo findPetInfoById(Long petInfoId){
        return petInfoRepository.findById(petInfoId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_PET_INFO));
    }


    private Inquiry findInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_INQUIRY));
    }


    /*
     * 전화번호와 펫 이름으로 Comparison 검색
     * @param content
     */
    public ComparisonResponse.ComparisonPageDto searchComparisons(String content, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        // '-' 제거
        String chagnedContent = content != null ? content.replaceAll("-", "") : null;
        Page<Comparison> comparisonPage = comparisonRepository.findAllByPhoneNumOrPetName(chagnedContent, pageable);

        return ComparisonConverter.toComparisonPageDto(comparisonPage);
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

    private void saveInsuranceHistory(Insurance insurance, int oldPremium, int newPremium) {
        // 보험 수정 이력 저장
        InsuranceHistory history = InsuranceHistoryConverter.createHistory(insurance, oldPremium, newPremium);
        insuranceHistoryRepository.save(history);
    }

    private Insurance findInsuranceById(Long insuranceId){
        return insuranceRepository.findByInsuranceId(insuranceId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.NOT_EXIST_INSURANCE));
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


}


