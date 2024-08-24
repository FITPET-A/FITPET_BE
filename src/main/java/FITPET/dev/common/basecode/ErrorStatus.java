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

    TEMP(400, "테스트용 ErrorStatus입니다.");

    /**
     * 400
     */

    /**
     * 404
     */


    private final int code;
    private final String message;
}