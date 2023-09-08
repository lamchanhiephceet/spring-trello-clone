package com.hn.springtrelloclone;

import com.hn.springtrelloclone.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class SpringTrelloCloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTrelloCloneApplication.class, args);
    }

}
