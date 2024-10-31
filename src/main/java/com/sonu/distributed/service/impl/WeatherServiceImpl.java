package com.sonu.distributed.service.impl;

import com.sonu.distributed.model.CurrentWeatherStatistics;
import com.sonu.distributed.model.DetailedWeatherStatistics;
import com.sonu.distributed.model.ForecastWeatherStatistics;
import com.sonu.distributed.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {
    @Inject
    @Named("open-weather-client")
    private WebClient webClient;

    @Override
    public CurrentWeatherStatistics getCurrentWeather(double lat, double lon) {
        Mono<CurrentWeatherStatistics> currentWeatherStatisticsMono = webClient.get()
                .uri("/weather?lat="+lat+"&lon="+lon+"&appid=9880fec00cb7f6784318764a9620a0c9")
                .retrieve()
                .bodyToMono(CurrentWeatherStatistics.class);
        CurrentWeatherStatistics currentWeatherStatistics;
        try {
            currentWeatherStatistics = currentWeatherStatisticsMono.block(Duration.of(5, ChronoUnit.SECONDS));
            log.info("fetched current weather [{}]", currentWeatherStatistics);
            return currentWeatherStatistics;
        } catch (Exception e) {
            log.error("Could not fetch current weather data, due to: ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public ForecastWeatherStatistics getForecastedWeather(double lat, double lon) {
        Mono<ForecastWeatherStatistics> currentWeatherStatisticsMono = webClient.get()
                .uri("/weather?lat="+lat+"&lon="+lon+"&appid=9880fec00cb7f6784318764a9620a0c9")
                .retrieve()
                .bodyToMono(ForecastWeatherStatistics.class);
        ForecastWeatherStatistics forecastWeatherStatistics;
        try {
            forecastWeatherStatistics = currentWeatherStatisticsMono.block(Duration.of(5, ChronoUnit.SECONDS));
            log.info("fetched forecasted weather [{}]", forecastWeatherStatistics);
            return forecastWeatherStatistics;
        } catch (Exception e) {
            log.error("Could not fetch current weather data, due to: ", e);
            throw new RuntimeException(e);
        }
    }
}