package FITPET.dev.service;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.enums.Company;
import FITPET.dev.common.enums.Status;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.utils.ExcelUtils;
import FITPET.dev.converter.InquiryConverter;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.converter.PetInfoConverter;
import FITPET.dev.converter.ProposalConverter;
import FITPET.dev.dto.response.InquiryResponse;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.PetInfoResponse;
import FITPET.dev.dto.response.ProposalResponse;
import FITPET.dev.entity.Inquiry;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.PetInfo;
import FITPET.dev.entity.Proposal;
import FITPET.dev.repository.InquiryRepository;
import FITPET.dev.repository.InsuranceRepository;
import FITPET.dev.repository.PetInfoRepository;
import FITPET.dev.repository.ProposalRepository;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final ExcelUtils excelUtils;

    /*
     * 회사별 보험 테이블 조회
     * @param page
     * @param companyStr
     * @return
     */
    public InsuranceResponse.InsuranceDetailPageDto getInsurances(int page, String companyStr) {

        // 페이지 크기, 페이지 번호 정보를 Pageable 객체에 설정
        Pageable pageable = PageRequest.of(page, 100);

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
     * 견적서 일괄 엑셀 다운로드
     * @param servletResponse
     */
    public void downloadPetInfos(HttpServletResponse servletResponse) {

        // 견적서 일괄 조회
        List<PetInfo> petInfoList = petInfoRepository.findAll();

        // excelDto로 타입 변경
        List<PetInfoResponse.PetInfoExcelDto> petInfoExcelDtoList = convertToPetInfoExcelDtoList(
                petInfoList);

        // excel 파일 다운로드
        excelUtils.downloadPetInfos(servletResponse, petInfoExcelDtoList);
    }


    /*
     * 견적 요청 상태 변경
     * @param petInfoId
     * @param status
     * @return
     */
    @Transactional
    public String patchPetInfoStatus(Long petInfoId, Status status){

        // 견적서 단일 조회
        PetInfo petInfo = findPetInfoById(petInfoId);

        // validate status
        Status currentStatus = petInfo.getStatus();
        if (currentStatus.getIndex() > status.getIndex())
            throw new GeneralException(ErrorStatus.INVALID_PATCH_PERIOR_STATUS);

        // patch status
        petInfo.updateStatus(status);
        return petInfo.getStatus().getLabel();
    }


    /*
     * 1:1 문의 내역 전체 조회
     * @return
     */
    public InquiryResponse.InquiryListDto getInquiries(){
        // 전체 1:1 문의 내역 조회
        List<Inquiry> inquiryList = inquiryRepository.findAllByOrderByCreatedAtAsc();

        return InquiryConverter.toInquiryListDto(inquiryList);
    }


    /*
     * 제휴문의 내역 전체 조회
     * @return
     */
    public ProposalResponse.ProposalListDto getProposals(){

        // 전체 제휴문의 내역 조회
        List<Proposal> proposalList = proposalRepository.findAllByOrderByCreatedAtAsc();

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


    /*
     * PetInfo를 조회
     * @param page, size, sort, startDate, endDate
     */
    public PetInfoResponse.PetInfoDetailPageDto getPetInfos(String startDate, String endDate, int page, String sort, Status status) {
        LocalDateTime start = parseDate(startDate, " 00:00:00");
        LocalDateTime end = parseDate(endDate, " 23:59:59");
        Sort sortOption = sort.equalsIgnoreCase("asc") ? Sort.by("createdAt").ascending() : Sort.by("createdAt").descending();
        int size = 20;
        Pageable pageable = PageRequest.of(page, size, sortOption);

        Page<PetInfo> petInfoPage = petInfoRepository.findAllByCreatedAtBetweenAndStatus(start, end, status, pageable);
        return PetInfoConverter.toPetInfoDetailPageDto(petInfoPage);
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


}





