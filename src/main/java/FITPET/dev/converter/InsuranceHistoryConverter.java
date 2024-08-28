package FITPET.dev.converter;

import FITPET.dev.entity.Insurance;
import FITPET.dev.entity.InsuranceHistory;

public class InsuranceHistoryConverter {

    public static InsuranceHistory createHistory(Insurance insurance, int oldValue, int newValue) {
        return InsuranceHistory.builder()
                .insurance(insurance)
                .oldValue(oldValue)
                .newValue(newValue)
                .build();
    }

}
