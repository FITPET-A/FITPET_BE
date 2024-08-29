package FITPET.dev.service;

import FITPET.dev.common.enums.InquiryStatus;
import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.enums.Company;
import FITPET.dev.common.enums.PetInfoStatus;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.utils.ExcelUtils;
import FITPET.dev.converter.InquiryConverter;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.converter.InsuranceHistoryConverter;
import FITPET.dev.converter.PetInfoConverter;
import FITPET.dev.converter.ProposalConverter;
import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.dto.response.InsuranceHistoryResponse;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.PetInfoResponse;
import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.Inquiry;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.InsuranceHistory;
import FITPET.dev.entity.PetInfo;
import FITPET.dev.entity.Proposal;
import FITPET.dev.repository.InquiryRepository;
import FITPET.dev.repository.InsuranceHistoryRepository;
import FITPET.dev.repository.InsuranceRepository;
import FITPET.dev.repository.PetInfoRepository;
import FITPET.dev.repository.ProposalRepository;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;
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
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdminService {

    private final InsuranceRepository insuranceRepository;
    private final PetInfoRepository petInfoRepository;
    private final InquiryRepository inquiryRepository;
    private final ProposalRepository proposalRepository;
    private final InsuranceHistoryRepository insuranceHistoryRepository;
    private final ExcelUtils excelUtils;

    /*
     * 회사별 보험 테이블 조회
     * @param page
     * @param companyStr
     * @return
     */
    public InsuranceResponse.InsuranceDetailPageDto getInsurances(int page, String companyStr) {

        // 페이지 크기, 페이지 번호 정보를 Pageable 객체에 설정
        Pageable pageable = PageRequest.of(page, 20);

        // company 문자열에 따라 회사별 Insurance 정보를 페이징 객체로 반환
        Page<Insurance> insurancePage = getInsurancePageByCompany(companyStr, pageable);

        // converter을 통해 pageDto로 타입 변경 후 리턴
        return InsuranceConverter.toInsuranceDetailPageDto(insurancePage);
    }


    /*
     * 회사별 보험 테이블 엑셀 다운로드
     * @param servletResponse
     * @param companyStr
     */
    public void downloadInsurances(HttpServletResponse servletResponse, String companyStr) {

        // company 문자열에 따라 회사별 Insurance 정보를 List로 반환
        List<Insurance> insuranceList = getInsuranceListByCompany(companyStr);

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
    public PetInfoResponse.PetInfoDetailPageDto getPetInfos(String startDate, String endDate, int page, PetInfoStatus petInfoStatus) {

        // 날짜 형식 변경
        LocalDateTime start = parseDate(startDate, " 00:00:00");
        LocalDateTime end = parseDate(endDate, " 23:59:59");

        // 견적 요청 리스트를 페이지 단위로 조회
        Pageable pageable = PageRequest.of(page, 20);
        Page<PetInfo> petInfoPage = petInfoRepository.findByCreatedAtBetweenAndStatus(start, end, petInfoStatus, pageable);

        return PetInfoConverter.toPetInfoDetailPageDto(petInfoPage);
    }


    /*
     * 견적 요청 내역 엑셀 다운로드
     * @param servletResponse
     */
    public void downloadPetInfos(HttpServletResponse servletResponse,
                                 String startDate, String endDate, PetInfoStatus petInfoStatus) {
        // 날짜 형식 변경
        LocalDateTime start = parseDate(startDate, " 00:00:00");
        LocalDateTime end = parseDate(endDate, " 23:59:59");

        // 견적서 요청 리스트 조회
        List<PetInfo> petInfoList = petInfoRepository.findByCreatedAtBetweenAndStatus(start, end, petInfoStatus);

        // excelDto로 타입 변경
        List<PetInfoResponse.PetInfoExcelDto> petInfoExcelDtoList = convertToPetInfoExcelDtoList(
                petInfoList);

        // excel 파일 다운로드
        excelUtils.downloadPetInfos(servletResponse, petInfoExcelDtoList);
    }


    /*
     * 견적 요청 상태 변경
     * @param petInfoId
     * @param petInfoStatus
     * @return
     */
    @Transactional
    public PetInfoResponse.PetInfoDetailDto patchPetInfoStatus(Long petInfoId, PetInfoStatus petInfoStatus){

        // 견적서 단일 조회
        PetInfo petInfo = findPetInfoById(petInfoId);

        // validate status
        PetInfoStatus currentPetInfoStatus = petInfo.getStatus();
        if (currentPetInfoStatus.getIndex() > petInfoStatus.getIndex())
            throw new GeneralException(ErrorStatus.INVALID_PATCH_PERIOR_STATUS);

        // patch status
        petInfo.updateStatus(petInfoStatus);
        return PetInfoConverter.toPetInfoDetailDto(petInfo);
    }


    /*
     * 1:1 문의 내역 조회
     * @param startDate
     * @param endDate
     * @param inquiryStatus
     * @return
     */
    public InquiryResponse.InquiryListDto getInquiries(String startDate, String endDate, InquiryStatus inquiryStatus){
        // 최소, 최대 조회 기간
        LocalDateTime minDateTime = LocalDateTime.of(2000, 1, 1, 0, 0);
        LocalDateTime maxDateTime = LocalDateTime.of(3999, 12, 31, 23, 59, 59);

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


    private Page<Insurance> getInsurancePageByCompany(String companyStr, Pageable pageable) {

        if (companyStr.equals("all")) {
            // 전체 insurance Page 반환
            return insuranceRepository.findAll(pageable);
        } else {
            // company 정보가 일치하는 insurance Page 반환
            Company company = getCompany(companyStr);
            return insuranceRepository.findByCompany(company, pageable);
        }
    }


    private List<Insurance> getInsuranceListByCompany(String companyStr) {

        if ("all".equals(companyStr)) {
            // 전체 insurance List 반환
            return insuranceRepository.findAll();
        } else {
            // company 정보가 일치하는 insurance List 반환
            Company company = getCompany(companyStr);
            return insuranceRepository.findByCompany(company);
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


    private List<PetInfoResponse.PetInfoExcelDto> convertToPetInfoExcelDtoList(
            List<PetInfo> petInfoList) {
        return petInfoList.stream()
                .map(PetInfoConverter::toPetInfoExcelDto)
                .toList();
    }


    private Company getCompany(String companyStr) {
        return Optional.ofNullable(Company.getCompany(companyStr))
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_COMPANY));
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
     * 전화번호와 펫 이름으로 PetInfo 검색
     * @param content
     */
    public PetInfoResponse.PetInfoDetailPageDto searchPetInfos(String content, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);

        // '-' 제거
        String chagnedContent = content != null ? content.replaceAll("-", "") : null;
        Page<PetInfo> petInfoPage = petInfoRepository.findAllByPhoneNumOrPetName(chagnedContent, pageable);

        return PetInfoConverter.toPetInfoDetailPageDto(petInfoPage);
    }


    /*
     * 보험료 수정
     * @param insuracneId
     * @param premium
     */
    @Transactional
    public InsuranceResponse.InsuranceDetailDto updateInsurancePremium(Long insuranceId, int premium){
        // 보험 객체 조회
        Insurance insurance = findInsuranceById(insuranceId);

        // 기존 보험료 저장
        int oldPremium = insurance.getPremium();

        // 보험료 업데이트
        insurance.updatepremium(premium);

        // 보험 정보 수정 이력 저장
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
}


