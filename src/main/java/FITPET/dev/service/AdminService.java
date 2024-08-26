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


@Service
@RequiredArgsConstructor
public class AdminService {
    private final InsuranceRepository insuranceRepository;
    private final ExcelUtils excelUtils;

    // 회사별 보험 테이블 조회
    public InsuranceResponse.InsuranceDetailPageDto getInsurances(int page, String companyStr){
        Pageable pageable = PageRequest.of(page, 100);

        if (companyStr.equals("all")){
            // 전체 조회
            Page<Insurance> insurancePage = insuranceRepository.findAll(pageable);
            return InsuranceConverter.toInsuranceDetailPageDto(insurancePage);
        }

        Company company = getCompany(companyStr);
        Page<Insurance> insurancePage = insuranceRepository.findByCompany(company, pageable);
        return InsuranceConverter.toInsuranceDetailPageDto(insurancePage);
    }

    // 회사별 보험 테이블 엑셀 다운로드
    public void downloadsInsurances(HttpServletResponse servletResponse, String companyStr){

        List<Insurance> insuranceList;

        if (companyStr.equals("all")){
            // 전체 데이터 다운로드
            insuranceList = insuranceRepository.findAll();
        } else {
            Company company = getCompany(companyStr);
            insuranceList = insuranceRepository.findByCompany(company);
        }

        List<InsuranceResponse.InsuranceDetailExcelDto> insuranceExcelDtoList = insuranceList.stream()
                .map(InsuranceConverter::toInsuranceDetailExcelDto)
                .toList();
        excelUtils.downloadInsurances(servletResponse, insuranceExcelDtoList);
    }

    private Company getCompany(String companyStr){
        Company company = Company.getCompany(companyStr);

        if (company == null)
            throw new GeneralException(ErrorStatus.INVALID_COMPANY);
        return company;
    }
}
