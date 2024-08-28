package FITPET.dev.common.status;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    /**
     *  Error Code
     *  400 : 잘못된 요청
     *  401 : JWT에 대한 오류
     *  403 : 요청한 정보에 대한 권한 없음.
     *  404 : 존재하지 않는 정보에 대한 요청.
     */

    /**
     * 400
     */
    // excel
    FAILURE_READ_EXCEL_FILE(400, "엑셀 파일을 읽는 과정에서 오류가 발생했습니다."),
    FAILURE_READ_EXCEL_SHEET(400, "엑셀 시트를 읽는 과정에서 오류가 발생했습니다."),

    // pet
    INVALID_PET_TYPE(400, "잘못된 품종 정보입니다."),

    // petinfo
    INVALID_PATCH_PERIOR_STATUS(400, "이전 상태로 상태를 변경할 수 없습니다."),
    INVALID_PETTYPE_WITH_DETAILTYPE(400, "요청하신 품종과 상세품종이 일치하지 않습니다."),

    // insurance
    INVALID_COMPANY(400, "잘못된 회사명입니다."),


    // validation
    INVALID_PHONE_NUMBER(400,"유효하지 않은 전화번호 형식입니다."),
    INVALID_EMAIL(400,"유효하지 않은 이메일 형식입니다."),
    INVALID_AGE(400,"유효하지 않은 나이 값입니다."),
    INVALID_AGE_OVER_10(400, "현재 펫보험은 만 10살 초과된 아이는 가입이 어렵습니다."),
    INVALID_DATE_FORMAT(400, "유효하지 않은 날짜 형식입니다."),

    // enum
    INVALID_COVERAGE_RATIO_VALUE(400, "유효하지 않은 보상 비율 값입니다."),
    INVALID_COMPENSATION_VALUE(400, "유효하지 않은 1일 보상 값입니다."),
    INVALID_DEDUCTIBLE_VALUE(400, "유효하지 않은 자부담 값입니다."),
    INVALID_RENEWAL_CYCLE_VALUE(400, "유효하지 않은 갱신주기 값입니다."),


    /**
     * 404
     * NOT_FOUND
     */
    // pet
    NOT_EXIST_PET(404, "존재하지 않는 품종입니다."),
    NOT_EXIST_PET_INFO(404, "존재하지 않는 펫 정보입니다."),

    // insurance
    NOT_EXIST_INSURANCE(404, "존재하지 않는 보험 정보입니다."),

    /**
     * 500
     */
    // excel
    FAILURE_RENDER_EXCEL_BODY(500, "엑셀 바디를 그리는 중 오류가 발생했습니다."),
    FAILURE_ENCODE_EXCEL_FILE_NAME(500, "엑셀 파일 이름을 인코딩 하는 중 오류가 발생했습니다.");
    


    private final int code;
    private final String message;
}