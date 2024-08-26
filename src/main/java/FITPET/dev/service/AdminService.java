package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.enums.Company;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.common.utils.ExcelUtils;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Insurance;
import FITPET.dev.repository.InsuranceRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final InsuranceRepository insuranceRepository;
    private final ExcelUtils excelUtils;

    // 회사별 보험 테이블 조회
    public InsuranceResponse.InsuranceDetailPageDto getInsurances(int page, String companyStr){

        // 페이지 크기, 페이지 번호 정보를 Pageable 객체에 설정
        Pageable pageable = PageRequest.of(page, 100);

        // company 문자열에 따라 회사별 Insurance 정보를 페이징 객체로 반환
        Page<Insurance> insurancePage = getInsurancePageByCompany(companyStr, pageable);

        // converter을 통해 pageDto로 타입 변경 후 리턴
        return InsuranceConverter.toInsuranceDetailPageDto(insurancePage);
    }


    // 회사별 보험 테이블 엑셀 다운로드
    public void downloadsInsurances(HttpServletResponse servletResponse, String companyStr) {

        // company 문자열에 따라 회사별 Insurance 정보를 List로 반환
        List<Insurance> insuranceList = getInsuranceListByCompany(companyStr);

        // excelDto로 타입 변경
        List<InsuranceResponse.InsuranceDetailExcelDto> insuranceExcelDtoList = convertToExcelDtoList(insuranceList);

        // excel 파일 다운로드
        excelUtils.downloadInsurances(servletResponse, insuranceExcelDtoList);
    }


    private Page<Insurance> getInsurancePageByCompany(String companyStr, Pageable pageable) {

        if (companyStr.equals("all")){
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


    private List<InsuranceResponse.InsuranceDetailExcelDto> convertToExcelDtoList(List<Insurance> insuranceList) {
        return insuranceList.stream()
                .map(InsuranceConverter::toInsuranceDetailExcelDto)
                .toList();
    }


    private Company getCompany(String companyStr) {
        return Optional.ofNullable(Company.getCompany(companyStr))
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_COMPANY));
    }

}
