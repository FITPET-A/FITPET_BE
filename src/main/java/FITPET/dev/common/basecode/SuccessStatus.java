package FITPET.dev.common.basecode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    SUCCESS(200, "응답에 성공했습니다.");

    private final int code;
    private final String message;

}