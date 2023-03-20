package sangmyung.chatprompt.Util.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Author : Jeeseob
 * @CreateAt : 2022/11/25
 */


@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {

        Info info = new Info()
                .version("v1.0.0")
                .title("ChatPrompt API")
                .description("API Description");

        return new OpenAPI()
                .info(info);
    }
}