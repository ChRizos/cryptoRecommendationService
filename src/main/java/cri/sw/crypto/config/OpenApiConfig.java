package cri.sw.crypto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cryptoApiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Crypto Recommendation Service API")
                        .description("Provides analytics and insights for crypto investments")
                        .version("v1.0"));
    }
}