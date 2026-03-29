package com.llm.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
public class OpenAiModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenAiModuleApplication.class, args);
    }

}
