package com.sonu.distributed.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter //used for aggregation processor
@ToString
public class WeatherAnomalyStatistics implements Serializable {
    private Float temperature;
    private Float feelsLike;
    private Float temperatureMin;
    private Float temperatureMax;
    private Float pressure;
    private Float humidity;
    private Long collectedTime;
    private Long processedTime;
    private Long aggregatedRecordCount;

    public WeatherAnomalyStatistics() {
        temperature = 0.0f;
        feelsLike = 0.0f;
        temperatureMin = 0.0f;
        temperatureMax = 0.0f;
        pressure = 0.0f;
        humidity = 0.0f;
        collectedTime = 0L;
        processedTime = 0L;
        aggregatedRecordCount = 0L;
    }

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
        collectedTime = value.getCurrentWeatherStatistics().getTimestamp();
        processedTime = System.currentTimeMillis();
        return this;
    }

    public synchronized WeatherAnomalyStatistics aggregateTemperatureDetails(WeatherAnomalyStatistics weatherAnomalyStatistics) {
        double temp = this.temperature * aggregatedRecordCount;
        double tempFeelsLike = this.feelsLike * aggregatedRecordCount;
        double tempMin = this.temperatureMin * aggregatedRecordCount;
        double tempMax = this.temperatureMax * aggregatedRecordCount;
        double pres = this.pressure * aggregatedRecordCount;
        double hum = this.humidity * aggregatedRecordCount;
        long divisor = ++aggregatedRecordCount;
        this.temperature = (float) ((temp + weatherAnomalyStatistics.getTemperature()) / divisor);
        this.feelsLike = (float) ((tempFeelsLike + weatherAnomalyStatistics.getFeelsLike()) / divisor);
        this.temperatureMin = (float) ((tempMin + weatherAnomalyStatistics.getTemperatureMin()) / divisor);
        this.temperatureMax = (float) ((tempMax + weatherAnomalyStatistics.getTemperatureMax()) / divisor);
        this.pressure = (float) ((pres + weatherAnomalyStatistics.getPressure()) / divisor);
        this.humidity = (float) ((hum + weatherAnomalyStatistics.getHumidity()) / divisor);
        return this;
    }
}
