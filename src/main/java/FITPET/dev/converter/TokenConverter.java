package FITPET.dev.converter;

import FITPET.dev.common.security.dto.AuthTokenDto;
import FITPET.dev.common.security.dto.TokenDto;

public class TokenConverter {
    public static TokenDto toTokenDto(Long userId){
        return TokenDto.builder()
                .userId(userId)
                .build();
    }

    public static AuthTokenDto toAuthTokenDto(String token) {
        return AuthTokenDto.builder()
                .token(token)
                .build();
    }
}
