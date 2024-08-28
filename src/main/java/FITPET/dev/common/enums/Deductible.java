package FITPET.dev.common.enums;

import FITPET.dev.common.status.ErrorStatus;
import FITPET.dev.common.exception.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 자부담 금액
@Getter
@RequiredArgsConstructor
public enum Deductible {
    ONE("1만원"), THREE("3만원"), FIVE("5만원");

    private final String label;

    public static Deductible getDeductible(String label) {
        if (label.contains("1만"))
            return ONE ;
        else if (label.contains("3만"))
            return THREE;
        else if (label.contains("5만"))
            return FIVE;
        else
            throw new GeneralException(ErrorStatus.INVALID_DEDUCTIBLE_VALUE);
    }
}
