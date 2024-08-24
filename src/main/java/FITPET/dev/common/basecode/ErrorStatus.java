package FITPET.dev.common.basecode;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    TEMP(400, "테스트용 ErrorStatus입니다.");

    private final int code;
    private final String message;
}