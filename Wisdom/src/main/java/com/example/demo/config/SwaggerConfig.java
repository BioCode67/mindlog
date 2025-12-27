package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration // "이것은 설정 파일입니다"라고 스프링에게 알려줌
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MindLog API 명세서")
                        .description("나의 생각과 지혜를 기록하고 관리하는 REST API입니다.")
                        .version("v1.0.0"));
    }
}