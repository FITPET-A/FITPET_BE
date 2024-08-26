package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Company {
    MERITZ("메리츠"), DB("DB"), HYUNDAE("현대"), KB("KB"), SAMSUNG("삼성");

    private final String label;

    public static Company getCompany(String label) {
        if (label.contains("메리츠"))
            return MERITZ ;
        else if (label.contains("DB"))
            return DB;
        else if (label.contains("현대"))
            return HYUNDAE;
        else if (label.contains("KB"))
            return KB;
        else if (label.contains("삼성"))
            return SAMSUNG;
        else
            return null;
    }
}
