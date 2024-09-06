package FITPET.dev.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 경로에 대해 CORS를 허용
                .allowedOrigins("https://localhost:3000")
                .allowedOrigins("https://3.38.244.36.nip.io")
                .allowedMethods("GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*") // 허용할 헤더
                .allowCredentials(true) // 자격 증명 허용
                .maxAge(3000);
    }
}
