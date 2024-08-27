package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
    PENDING("응대 전", 0), SEND("발송 완료", 1), CONSULTING("상담 중", 2), COMPLETED("가입 완료", 3);

    private final String label;
    private final int index;
}
