package FITPET.dev.common.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    SUCCESS(200, "응답에 성공했습니다."),

    // insurance
    GET_INSURANCE_PREMIUM(200, "회사별 보험료 조회에 성공했습니다."),
    GET_INSURANCE_TABLE(200, "회사별 보험 테이블 조회에 성공했습니다."),
    UPDATE_INSURANCE_SUCCESS(200, "보험료 수정에 성공했습니다."),

    // pet
    GET_DETAILTYPE_LIST(200, "상세품종명 리스트 조회에 성공했습니다."),


    // petinfo
    PATCH_PET_INFO_STATUS(200, "견적서 상태 변경에 성공했습니다."),
    GET_PETINFO_TABLE(200, "견적서 요청 내역 조회에 성공했습니다."),
    SEARCH_PET_INFO(200, "견적서 내용 검색에 성공했습니다."),

    // inquiry
    POST_INQUIRY(200, "1:1 문의 전송에 성공했습니다."),
    GET_INQUIRY(200, "1:1 문의 내역 조회에 성공했습니다."),

    // proposal
    POST_PROPOSAL(200, "제휴제안 전송에 성공했습니다."),
    GET_PROPOSAL(200, "제휴제안 내역 조회에 성공했습니다."),;

    private final int code;
    private final String message;

}