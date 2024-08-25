package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 1일 보상 금액
@Getter
@RequiredArgsConstructor
public enum Compensation {
    FIFTEEN("15만"), THIRTY("30만");

    private final String label;

    public static Compensation getCompensation(String label) {
        if (label.equals("15만"))
            return FIFTEEN ;
        else
            return THIRTY;
    }
}
