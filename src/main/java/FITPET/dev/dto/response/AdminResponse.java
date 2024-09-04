package FITPET.dev.dto.response;

import FITPET.dev.common.security.dto.AuthTokenDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AdminResponse {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignDto {
        private AuthTokenDto accessToken;
        private AuthTokenDto refreshToken;
    }
}