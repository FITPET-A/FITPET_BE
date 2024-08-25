package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 자부담 금액
@Getter
@RequiredArgsConstructor
public enum Deductible {
    ONE("1만원"), THREE("3만원");

    private final String label;

    public static Deductible getDeductible(String label) {
        if (label.equals("1만원"))
            return ONE ;
        else
            return THREE;
    }
}
