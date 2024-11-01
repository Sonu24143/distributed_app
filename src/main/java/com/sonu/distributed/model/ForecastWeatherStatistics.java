package com.sonu.distributed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sonu.distributed.model.entity.WeatherDescriptionEntity;
import com.sonu.distributed.model.entity.WeatherStatsEntity;
import com.sonu.distributed.model.entity.WindStatsEntity;
import com.sonu.distributed.util.ModelConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class ForecastWeatherStatistics extends DetailedWeatherStatistics implements ModelConverter<WeatherStatsEntity> {
    @JsonAlias("dt")
    private long timestamp;

    @JsonAlias("pop")
    private float probabilityOfPrecipitation;

    @Override
    public WeatherStatsEntity convert() {
        WindStatsEntity windStatsEntity = ( getWindStatistics() != null) ? WindStatsEntity.builder()
                .speed(getWindStatistics().getSpeed())
                .degree(getWindStatistics().getDegree())
                .gust(getWindStatistics().getGust())
                .build() : WindStatsEntity.builder().build();
        Set<WeatherDescriptionEntity> weatherDescriptionEntities = getWeather().stream()
                .map(it -> WeatherDescriptionEntity.builder()
                .type(it.getType())
                .description(it.getDescription())
                .build()).collect(Collectors.toSet());
        WeatherStatsEntity weatherStatsEntity = WeatherStatsEntity.builder()
                .temperature(getWeatherStatistics().getTemperature())
                .feelsLike(getWeatherStatistics().getFeelsLike())
                .temperatureMin(getWeatherStatistics().getTemperatureMin())
                .temperatureMax(getWeatherStatistics().getTemperatureMax())
                .pressure(getWeatherStatistics().getPressure())
                .humidity(getWeatherStatistics().getHumidity())
                .visibility(getVisibility())
                .groundLevel(getWeatherStatistics().getGroundLevel())
                .seaLevel(getWeatherStatistics().getSeaLevel())
                .rainMm((getRainStatistics() != null) ? getRainStatistics().getMmPerHour() : null)
                .cloudiness((getCloudStatistics() != null) ? getCloudStatistics().getCloudinessPercentage() : null)
                .precipitation(getProbabilityOfPrecipitation())
                .timestamp(getTimestamp() * 1000)
                .createdTime(System.currentTimeMillis())
                .windStatsEntity(windStatsEntity)
                .weatherDescriptionEntities(weatherDescriptionEntities)
                .build();
        windStatsEntity.setWeatherStatsEntity(weatherStatsEntity);
        weatherDescriptionEntities.forEach( it -> it.setWeatherStatsEntity(weatherStatsEntity));
        return weatherStatsEntity;
    }
}
