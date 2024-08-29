package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PetInfoStatus {
    PENDING("응대 전", 0),
    SEND("견적서 발송", 1),
    CONSULTING("상담 중", 2),
    CONSULTATION_COMPLETED("상담 완료", 3),
    COMPLETED("가입 완료", 4);

    private final String label;
    private final int index;
}
