package FITPET.dev.service;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.enums.Company;
import FITPET.dev.common.exception.GeneralException;
import FITPET.dev.converter.InsuranceConverter;
import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.entity.Insurance;
import FITPET.dev.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final InsuranceRepository insuranceRepository;

    // 회사별 보험 테이블 조회
    public InsuranceResponse.InsuranceDetailListDto getInsurances(String companyStr){
        if (companyStr.equals("all")){
            // 전체 조회
            List<Insurance> insuranceList = insuranceRepository.findAll();
            return InsuranceConverter.toInsuranceDetailListDto(insuranceList);
        }

        Company company = getCompany(companyStr);
        List<Insurance> insuranceList = insuranceRepository.findByCompany(company);
        return InsuranceConverter.toInsuranceDetailListDto(insuranceList);
    }

    private Company getCompany(String companyStr){
        Company company = Company.getCompany(companyStr);

        if (company == null)
            throw new GeneralException(ErrorStatus.INVALID_COMPANY);
        return company;
    }
}
