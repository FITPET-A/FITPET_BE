package FITPET.dev.common.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

    @OpenAPIDefinition(
            info = @Info(title="FITPET",
                    description = "큐시즘 Pit a Pat팀 FITPET 기업 프로젝트",
                    version = "v1")
    )
    @Configuration
    public class SwaggerConfig {
        @Bean
        public OpenAPI OpenAPI() {
            return new OpenAPI()
                    .components(new Components());
        }

    }


