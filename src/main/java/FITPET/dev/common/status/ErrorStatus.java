package FITPET.dev.common.status;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
    FAILURE_READ_EXCEL_FILE(HttpStatus.BAD_REQUEST, 400, "엑셀 파일을 읽는 과정에서 오류가 발생했습니다."),
    FAILURE_READ_EXCEL_SHEET(HttpStatus.BAD_REQUEST, 400, "엑셀 시트를 읽는 과정에서 오류가 발생했습니다."),

    // pet
    INVALID_PET_TYPE(HttpStatus.BAD_REQUEST, 400, "잘못된 품종 정보입니다."),

    // petinfo
    INVALID_PATCH_PERIOR_STATUS(HttpStatus.BAD_REQUEST, 400, "이전 상태로 상태를 변경할 수 없습니다."),
    INVALID_PETTYPE_WITH_DETAILTYPE(HttpStatus.BAD_REQUEST, 400, "요청하신 품종과 상세품종이 일치하지 않습니다."),

    // insurance
    INVALID_COMPANY(HttpStatus.BAD_REQUEST, 400, "잘못된 회사명입니다."),
    ALREADY_DELETED_INSURANCE(HttpStatus.BAD_REQUEST, 400, "이미 삭제된 보험 정보입니다."),

    // validation
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, 400,"유효하지 않은 전화번호 형식입니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, 400,"유효하지 않은 이메일 형식입니다."),
    INVALID_AGE(HttpStatus.BAD_REQUEST, 400,"유효하지 않은 나이 값입니다."),
    INVALID_AGE_OVER_10(HttpStatus.BAD_REQUEST, 400, "현재 펫보험은 만 10살 초과된 아이는 가입이 어렵습니다."),
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 날짜 형식입니다."),

    // enum
    INVALID_COVERAGE_RATIO_VALUE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 보상 비율 값입니다."),
    INVALID_COMPENSATION_VALUE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 1일 보상 값입니다."),
    INVALID_DEDUCTIBLE_VALUE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 자부담 값입니다."),
    INVALID_RENEWAL_CYCLE_VALUE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 갱신주기 값입니다."),

    // admin
    ALREADY_EXIST_ADMIN_ACCOUNT(HttpStatus.BAD_REQUEST, 400, "이미 가입된 관리자 계정이 존재합니다."),
    INVALID_ADMIN_SIGN_UP(HttpStatus.BAD_REQUEST, 400, "관리자 계정으로 가입할 수 없는 정보입니다."),
    INVALID_PASSWORD_MATCH(HttpStatus.BAD_REQUEST, 400, "비밀번호가 일치하지 않습니다."),


    /**
     * 404
     * NOT_FOUND
     */
    // pet
    NOT_EXIST_PET(HttpStatus.NOT_FOUND, 404, "존재하지 않는 품종입니다."),
    NOT_EXIST_PET_INFO(HttpStatus.NOT_FOUND, 404, "존재하지 않는 펫 정보입니다."),

    // insurance
    NOT_EXIST_INSURANCE(HttpStatus.NOT_FOUND, 404, "존재하지 않는 보험 정보입니다."),
    NOT_EXIST_PREMIUM_HISTORY(HttpStatus.NOT_FOUND, 404, "존재하지 않는 보험료 히스토리 정보입니다."),

    // inquiry
    NOT_EXIST_INQUIRY(HttpStatus.NOT_FOUND, 404, "존재하지 않는 1:1 문의 정보입니다."),

    // proposal
    NOT_EXIST_PROPOSAL(HttpStatus.NOT_FOUND, 400, "존재하지 않는 제휴 문의 내역입니다."),

    // comparison
    NOT_EXIST_COMPARISON(HttpStatus.NOT_FOUND, 404, "존재하지 않는 견적 요청입니다."),

    // referSite
    NOT_EXIST_REFERSITE(HttpStatus.NOT_FOUND, 404, "존재하지 않는 참조 사이트입니다."),

    NOT_EXIST_ADMIN(HttpStatus.NOT_FOUND, 404, "존재하지 않는 관리자 정보입니다."),



    /**
     * 500
     */
    // excel
    FAILURE_RENDER_EXCEL_BODY(HttpStatus.INTERNAL_SERVER_ERROR, 500, "엑셀 바디를 그리는 중 오류가 발생했습니다."),
    FAILURE_ENCODE_EXCEL_FILE_NAME(HttpStatus.INTERNAL_SERVER_ERROR, 500, "엑셀 파일 이름을 인코딩 하는 중 오류가 발생했습니다."),

    // pdf
    FAILURE_ENCODE_PDF_FILE_NAME(HttpStatus.INTERNAL_SERVER_ERROR, 500, "PDF 파일 이름을 인코딩 하는 중 오류가 발생했습니다.");
    ;
    

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}