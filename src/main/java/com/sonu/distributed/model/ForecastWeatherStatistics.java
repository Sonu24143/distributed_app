package com.sonu.distributed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.sonu.distributed.common.Constants;
import com.sonu.distributed.model.entity.WeatherDescriptionEntity;
import com.sonu.distributed.model.entity.WeatherStatsEntity;
import com.sonu.distributed.model.entity.WindStatsEntity;
import com.sonu.distributed.util.ModelConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class ForecastWeatherStatistics extends DetailedWeatherStatistics implements ModelConverter<List<WeatherStatsEntity>> {
    @JsonAlias("dt")
    private long timestamp;

    @JsonAlias("pop")
    private float probabilityOfPrecipitation;

    public ForecastWeatherStatistics(List<Weather> weather, WeatherStatistics weatherStatistics, int visibility, WindStatistics windStatistics, RainStatistics rainStatistics, CloudStatistics cloudStatistics, DayTimeStatistics dayTimeStatistics, long timestamp, float probabilityOfPrecipitation) {
        super(weather, weatherStatistics, visibility, windStatistics, rainStatistics, cloudStatistics, dayTimeStatistics);
        this.timestamp = timestamp;
        this.probabilityOfPrecipitation = probabilityOfPrecipitation;
    }

    public ForecastWeatherStatistics(long timestamp, float probabilityOfPrecipitation) {
        this.timestamp = timestamp;
        this.probabilityOfPrecipitation = probabilityOfPrecipitation;
    }

    @Override
    public List<WeatherStatsEntity> convert() {
        WeatherStatsEntity i1 = generateEntity();
        WeatherStatsEntity i2 = generateEntity();
        WeatherStatsEntity i3 = generateEntity();
        i2.setCollectionTime(i1.getCollectionTime() + Constants.HOUR_IN_MILLIS);
        i3.setCollectionTime(i2.getCollectionTime() + Constants.HOUR_IN_MILLIS);
        return Arrays.asList(i1, i2, i3);
    }

    private WeatherStatsEntity generateEntity() {
        WindStatsEntity windStatsEntity = new WindStatsEntity();
        if( getWindStatistics() != null) {
            windStatsEntity.setSpeed(getWindStatistics().getSpeed());
            windStatsEntity.setDegree(getWindStatistics().getDegree());
            windStatsEntity.setGust(getWindStatistics().getGust());
        }
        Set<WeatherDescriptionEntity> weatherDescriptionEntities = getWeather().stream()
                .map(it -> {
                            WeatherDescriptionEntity weatherDescriptionEntity = new WeatherDescriptionEntity();
                            weatherDescriptionEntity.setType(it.getType());
                            weatherDescriptionEntity.setDescription(it.getDescription());
                            return weatherDescriptionEntity;
                        }).collect(Collectors.toSet());
        WeatherStatsEntity weatherStatsEntity = new WeatherStatsEntity();
        weatherStatsEntity.setTemperature(getWeatherStatistics().getTemperature());
        weatherStatsEntity.setFeelsLike(getWeatherStatistics().getFeelsLike());
        weatherStatsEntity.setTemperatureMin(getWeatherStatistics().getTemperatureMin());
        weatherStatsEntity.setTemperatureMax(getWeatherStatistics().getTemperatureMax());
        weatherStatsEntity.setPressure(getWeatherStatistics().getPressure());
        weatherStatsEntity.setHumidity(getWeatherStatistics().getHumidity());
        weatherStatsEntity.setVisibility(getVisibility());
        weatherStatsEntity.setGroundLevel(getWeatherStatistics().getGroundLevel());
        weatherStatsEntity.setSeaLevel(getWeatherStatistics().getSeaLevel());
        weatherStatsEntity.setRainMm((getRainStatistics() != null) ? getRainStatistics().getMmPerHour() : null);
        weatherStatsEntity.setCloudiness((getCloudStatistics() != null) ? getCloudStatistics().getCloudinessPercentage() : null);
        weatherStatsEntity.setPrecipitation(getProbabilityOfPrecipitation());
        weatherStatsEntity.setCollectionTime(getTimestamp() * 1000);
        weatherStatsEntity.setCreatedTime(System.currentTimeMillis());
        weatherStatsEntity.setWindStatsEntity(windStatsEntity);
        weatherStatsEntity.setWeatherDescriptionEntities(weatherDescriptionEntities);
        windStatsEntity.setWeatherStatsEntity(weatherStatsEntity);
        weatherDescriptionEntities.forEach( it -> it.setWeatherStatsEntity(weatherStatsEntity));
        return weatherStatsEntity;
    }
}
