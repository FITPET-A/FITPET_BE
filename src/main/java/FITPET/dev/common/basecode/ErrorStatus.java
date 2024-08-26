package FITPET.dev.common.basecode;


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
    FAILURE_READ_EXCEL_FILE(400, "엑셀 파일을 읽는 과정에서 오류가 발생했습니다."),
    FAILURE_READ_EXCEL_SHEET(400, "엑셀 시트를 읽는 과정에서 오류가 발생했습니다."),
    INVALID_PET_TYPE(400, "잘못된 품종 정보입니다."),

    /**
     * 404
     * NOT_FOUND
     */
    NOT_EXIST_PET(404, "존재하지 않는 품종입니다.");


    private final int code;
    private final String message;
}