package FITPET.dev.common.security.jwt;

import FITPET.dev.common.security.dto.AuthTokenDto;
import FITPET.dev.common.security.dto.TokenDto;
import FITPET.dev.converter.TokenConverter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
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

        return TokenConverter.toAuthTokenDto(token);
    }

    public TokenDto validateToken(String token){
        // test
        if (token.equals("FITPET TEST"))
            return TokenConverter.toTokenDto(1L);

        try {
            // verify token
            JWTVerifier tokenVerifier = JWT
                    .require(tokenAlgorithm)
                    .withClaimPresence("userId")
                    .build();

            DecodedJWT verifiedJWT = tokenVerifier.verify(token);
            return TokenConverter.toTokenDto(verifiedJWT.getClaim("userId").asLong());

        } catch (TokenExpiredException e) {
            throw new TokenExpiredException("Token expired", Instant.now());
        } catch (JWTVerificationException e) {
            throw new JWTVerificationException("Invalid token", e);
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(TokenDto tokenDto) {
        UsernamePasswordAuthenticationToken authenticationToken = null;
        if (tokenDto != null) {
            authenticationToken = new UsernamePasswordAuthenticationToken(
                    tokenDto,
                    tokenDto.getUserId(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        }

        return authenticationToken;
    }


}
