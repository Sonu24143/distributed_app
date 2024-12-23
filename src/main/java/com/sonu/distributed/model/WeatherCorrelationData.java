package com.sonu.distributed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WeatherCorrelationData {
    private ForecastWeatherStatistics forecastWeatherStatistics;
    private CurrentWeatherStatistics currentWeatherStatistics;
}
