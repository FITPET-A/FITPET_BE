package FITPET.dev.common.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import FITPET.dev.common.response.ApiResponse;

public class GeneralExceptionAdvice {
    @ExceptionHandler(value = { GeneralException.class })
    protected ApiResponse<String> handleException(GeneralException e) {
        return ApiResponse.FailureResponse(e.getErrorStatus());
    }
}
