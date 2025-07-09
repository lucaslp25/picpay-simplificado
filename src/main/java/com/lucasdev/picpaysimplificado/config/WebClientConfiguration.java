package com.lucasdev.picpaysimplificado.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {

        return builder.
                baseUrl("https://util.devi.tools/api/v2/authorize").
                defaultHeader("Accept", "application/json").
                build();
    }

    @Bean
    public WebClient webClient2(WebClient.Builder builder) {

        return builder.
                baseUrl("https://util.devi.tools/api/v1/notify")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

    }
}