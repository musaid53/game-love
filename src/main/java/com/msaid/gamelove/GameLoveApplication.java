package com.msaid.gamelove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.reactive.config.EnableWebFlux;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableWebFlux
@EnableConfigurationProperties
@EnableJpaAuditing
@EnableSwagger2
@Import(SpringDataRestConfiguration.class)
public class GameLoveApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameLoveApplication.class, args);
    }

}
