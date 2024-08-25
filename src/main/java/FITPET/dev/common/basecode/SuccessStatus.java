package FITPET.dev.common.basecode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    SUCCESS(200, "응답에 성공했습니다."),
    GET_INSURANCE_PREMIUM(200, "회사별 보험료 조회에 성공했습니다.");

    private final int code;
    private final String message;

}