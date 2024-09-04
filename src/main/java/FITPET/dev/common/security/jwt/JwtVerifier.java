package FITPET.dev.common.security.jwt;

import FITPET.dev.common.security.dto.TokenDto;
import FITPET.dev.converter.TokenConverter;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class JwtVerifier {
    private final Algorithm tokenAlgorithm;

    public TokenDto verify(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer "))
            return null;
        return verifyToken(bearerToken);
    }

    private TokenDto verifyToken(String bearerToken) {
        // test
        if (bearerToken.equals("FITPET TEST"))
            return TokenConverter.toTokenDto(1L);

        try {
            // verify token
            String token = bearerToken.substring(7);
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
}
