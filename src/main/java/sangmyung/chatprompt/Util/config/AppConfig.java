package sangmyung.chatprompt.Util.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sangmyung.chatprompt.Util.formatter.LocalDateFormatter;
import sangmyung.chatprompt.Util.formatter.LocalDateTimeFormatter;

import javax.persistence.EntityManager;

/**
 * @Author : Jeeseob
 * @CreateAt : 2022/11/25
 *
 */

@Configuration
@RequiredArgsConstructor
public class AppConfig {
    private final EntityManager em;

    @Bean
    public LocalDateFormatter localDateFormatter() {
        return new LocalDateFormatter();
    }

    @Bean
    public LocalDateTimeFormatter localDateTimeFormatter() {
        return new LocalDateTimeFormatter();
    }

//    @Bean
//    public JPAQueryFactory queryFactory() {
//        return new JPAQueryFactory(em);
//    }

}
