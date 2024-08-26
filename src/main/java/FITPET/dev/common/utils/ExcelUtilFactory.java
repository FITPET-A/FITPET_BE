package FITPET.dev.common.utils;

import FITPET.dev.dto.response.InsuranceResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ExcelUtilFactory {
    void downloadInsurances(HttpServletResponse response, List<InsuranceResponse.InsuranceDetailExcelDto> data);
}
