package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 보상 비율
@Getter
@RequiredArgsConstructor
public enum CoverageRatio {
    SEVENTY_PERCENT("70%"), EIGHTY_PERCENT("80%"), NINETY_PERCENT("90%");

    private final String label;
}
