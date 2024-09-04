package FITPET.dev.common.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthTokenDto {
    private String token;
    private Long expiredIn;
}