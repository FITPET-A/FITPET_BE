package FITPET.dev.common.enums;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 갱신 주기
@Getter
@RequiredArgsConstructor
public enum RenewalCycle {
    THREE_YEARS("3년"), FIVE_YEARS("5년"), SEVEN_YEARS("7년"), TEN_YEARS("10년");

    private final String label;

    public static RenewalCycle getRenewalCycle(String label) {
        if (label.contains("3년"))
            return THREE_YEARS ;
        else if (label.contains("5년"))
            return FIVE_YEARS;
        else if (label.contains("7년"))
            return SEVEN_YEARS;
        else if (label.contains("10년"))
            return TEN_YEARS;
        else
            throw new GeneralException(ErrorStatus.INVALID_RENEWAL_CYCLE_VALUE);
    }
}


