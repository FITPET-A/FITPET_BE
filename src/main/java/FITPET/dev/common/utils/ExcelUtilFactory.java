package FITPET.dev.common.utils;

import FITPET.dev.dto.response.InsuranceResponse;
import FITPET.dev.dto.response.ComparisonResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ExcelUtilFactory {
    void downloadInsurances(HttpServletResponse response, List<InsuranceResponse.InsuranceExcelDto> data);
    void downloadComparisons(HttpServletResponse response, List<ComparisonResponse.ComparisonExcelDto> data);
}
