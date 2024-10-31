package com.sonu.distributed.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import javax.inject.Named;

@Configuration
public class OpenWeatherClientConfig {
    @Value("${openweather.base.url}")
    private String currentWeatherUrl;

    @Bean
    @Named("open-weather-client")
    public WebClient getCurrentWeatherClient() {
        return WebClient.builder().baseUrl(currentWeatherUrl).build();
    }
}
