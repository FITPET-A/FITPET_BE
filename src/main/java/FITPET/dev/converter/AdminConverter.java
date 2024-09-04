package FITPET.dev.converter;

import FITPET.dev.common.security.dto.AuthTokenDto;
import FITPET.dev.dto.response.AdminResponse;
import FITPET.dev.entity.Admin;

public class AdminConverter {
    public static Admin toAdmin(String id, String password, String adminName){
        return Admin.builder()
                .id(id)
                .password(password)
                .adminName(adminName)
                .build();
    }

    public static AdminResponse.SignDto toSignDto(AuthTokenDto accessToken, AuthTokenDto refreshToken){
        return AdminResponse.SignDto.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }
}
