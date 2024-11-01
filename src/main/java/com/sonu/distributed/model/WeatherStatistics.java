package com.sonu.distributed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WeatherStatistics {
    @JsonAlias("temp")
    private Float temperature;

    @JsonAlias("feels_like")
    private Float feelsLike;

    @JsonAlias("temp_min")
    private Float temperatureMin;

    @JsonAlias("temp_max")
    private Float temperatureMax;

    @JsonAlias("pressure")
    private Float pressure;

    @JsonAlias("humidity")
    private Float humidity;

    @JsonAlias("sea_level")
    private Float seaLevel;

    @JsonAlias("grnd_level")
    private Float groundLevel;
}
