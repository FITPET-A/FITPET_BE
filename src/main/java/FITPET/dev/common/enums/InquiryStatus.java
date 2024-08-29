package FITPET.dev.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InquiryStatus {
    PENDING("답변 대기", 0), CONSULTING("진행 중", 1), COMPLETED("응대 완료", 2);

    private final String label;
    private final int index;
}
