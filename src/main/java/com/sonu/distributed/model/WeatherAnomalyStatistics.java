package com.sonu.distributed.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WeatherAnomalyStatistics {
    private Float temperature;
    private Float feelsLike;
    private Float temperatureMin;
    private Float temperatureMax;
    private Float pressure;
    private Float humidity;

    public WeatherAnomalyStatistics apply(WeatherCorrelationData value) {
        if(value.getForecastWeatherStatistics() == null || value.getCurrentWeatherStatistics() == null) {
            return this;
        }
        WeatherStatistics current = value.getCurrentWeatherStatistics().getWeatherStatistics();
        WeatherStatistics forecast = value.getForecastWeatherStatistics().getWeatherStatistics();
        if(current == null || forecast == null) {
            return this;
        }
        temperature = forecast.getTemperature() - current.getTemperature();
        feelsLike = forecast.getFeelsLike() - current.getFeelsLike();
        temperatureMin = forecast.getTemperatureMin() - current.getTemperatureMin();
        temperatureMax = forecast.getTemperatureMax() - current.getTemperatureMax();
        pressure = forecast.getPressure() - current.getPressure();
        humidity = forecast.getHumidity() - current.getHumidity();
        return this;
    }
}
