package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 보상 비율
@Getter
@RequiredArgsConstructor
public enum CoverageRatio {
    SEVENTY_PERCENT("70%"), EIGHTY_PERCENT("80%"), NINETY_PERCENT("90%");

    private final String label;

    public static CoverageRatio getCoverageRatio(String label) {
        if (label.equals("70%"))
            return SEVENTY_PERCENT ;
        else if (label.equals("80%"))
            return EIGHTY_PERCENT;
        else
            return NINETY_PERCENT;
    }
}
