package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    PENDING("응대 전"), SEND("발송 완료"), CONSULTING("상담 중"), COMPLETED("가입 완료");

    private final String label;
}
