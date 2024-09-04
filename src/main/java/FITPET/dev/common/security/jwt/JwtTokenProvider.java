package FITPET.dev.common.security.jwt;

import FITPET.dev.common.security.dto.AuthTokenDto;
import FITPET.dev.converter.TokenConverter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final Algorithm tokenAlgorithm;
    @Value("${app.jwt.accessTokenValidMS}") private Long accessTokenValidMilliseconds;
    @Value("${app.jwt.refreshTokenValidMS}") private Long refreshTokenValidMilliseconds;

    public AuthTokenDto createAccessToken(Long id) {
        return createToken(id, accessTokenValidMilliseconds);
    }

    public AuthTokenDto createRefreshToken(Long id) {
        return createToken(id, refreshTokenValidMilliseconds);
    }

    private AuthTokenDto createToken(Long id, Long tokenValidMilliseconds){
        Date expiredAt = new Date(System.currentTimeMillis() + tokenValidMilliseconds);

        String token = JWT.create()
                .withClaim("userId", id)
                .withExpiresAt(expiredAt)
                .sign(tokenAlgorithm);

        return TokenConverter.toAuthTokenDto(token, tokenValidMilliseconds);
    }

}
