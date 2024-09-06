package FITPET.dev.common.config;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtAlgorithmConfiguration {
    @Bean
    public Algorithm tokenAlgorithm() {
        String secret = "FITPET";
        return Algorithm.HMAC256(secret);
    }
}
