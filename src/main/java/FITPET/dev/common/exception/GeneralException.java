package FITPET.dev.common.exception;

import FITPET.dev.common.basecode.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException{
    private final ErrorStatus errorStatus;
}
