package FITPET.dev.common.exception;

import FITPET.dev.common.status.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{
    private final ErrorStatus errorStatus;
}
