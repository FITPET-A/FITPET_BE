package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 갱신 주기
@Getter
@RequiredArgsConstructor
public enum RenewalCycle {
    THREE_YEARS("3년"),
    FIVE_YEARS("5년");

    private final String label;
}


