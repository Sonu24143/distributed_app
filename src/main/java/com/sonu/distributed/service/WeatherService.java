package com.sonu.distributed.service;

import com.sonu.distributed.model.CurrentWeatherStatistics;
import com.sonu.distributed.model.DetailedWeatherStatistics;
import com.sonu.distributed.model.ForecastWeatherStatistics;

public interface WeatherService {
    CurrentWeatherStatistics getCurrentWeather(double lat, double lon);
    ForecastWeatherStatistics getForecastedWeather(double lat, double lon);
}
