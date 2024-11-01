package com.sonu.distributed.service;

import com.sonu.distributed.model.CurrentWeatherStatistics;
import com.sonu.distributed.model.DetailedWeatherStatistics;
import com.sonu.distributed.model.ForecastWeatherStatistics;
import com.sonu.distributed.model.ForecastWeatherStatisticsCollection;

public interface WeatherService {
    CurrentWeatherStatistics getCurrentWeather(double lat, double lon);
    ForecastWeatherStatisticsCollection getForecastedWeather(double lat, double lon);
}
