package team.mosk.api.server.global.vo;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class NowProjectPathIs {

    @Bean
    public static String getPath() {
        return System.getProperty("user.dir");
    }
}
