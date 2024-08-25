package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Company {
    MERITZ("메리츠"), DB("DB"), HYUNDAE("현대"), KB("KB"), SAMSUNG("삼성");

    private final String label;
}
