package FITPET.dev.converter;

import FITPET.dev.dto.response.InsuranceHistoryResponse;
import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.InsuranceHistory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class InsuranceHistoryConverter {
    public static String formatDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public static InsuranceHistory createHistory(Insurance insurance, int oldValue, int newValue) {
        return InsuranceHistory.builder()
                .insurance(insurance)
                .oldValue(oldValue)
                .newValue(newValue)
                .build();
    }

    public static InsuranceHistoryResponse toResponse(InsuranceHistory insuranceHistory) {
        return InsuranceHistoryResponse.builder()
                .insuranceHistoryId(insuranceHistory.getInsuranceHistoryId())
                .oldValue(insuranceHistory.getOldValue())
                .newValue(insuranceHistory.getNewValue())
                .updatedAt(formatDateTime(insuranceHistory.getCreatedAt()))
                .build();
    }

    public static List<InsuranceHistoryResponse> toResponseList(List<InsuranceHistory> insuranceHistories) {
        return insuranceHistories.stream()
                .map(InsuranceHistoryConverter::toResponse)
                .collect(Collectors.toList());
    }



}
