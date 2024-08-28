package FITPET.dev.common.enums;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 갱신 주기
@Getter
@RequiredArgsConstructor
public enum RenewalCycle {
    THREE_YEARS("3년"),
    FIVE_YEARS("5년");

    private final String label;

    public static RenewalCycle getRenewalCycle(String label) {
        if (label.equals("3년"))
            return THREE_YEARS ;
        else if (label.equals("5년"))
            return FIVE_YEARS;
        else
            throw new GeneralException(ErrorStatus.INVALID_RENEWAL_CYCLE_VALUE);
    }
}


