package FITPET.dev.common.enums;

import FITPET.dev.common.basecode.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 보상 비율
@Getter
@RequiredArgsConstructor
public enum CoverageRatio {
    SEVENTY_PERCENT("70%"), EIGHTY_PERCENT("80%"), NINETY_PERCENT("90%");

    private final String label;

    public static CoverageRatio getCoverageRatio(String label) {
        if (label.contains("70"))
            return SEVENTY_PERCENT ;
        else if (label.contains("80"))
            return EIGHTY_PERCENT;
        else if (label.contains("90"))
            return NINETY_PERCENT;
        else
            throw new GeneralException(ErrorStatus.INVALID_COVERAGE_RATIO_VALUE);
    }
}
