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
    private float temperature;

    @JsonAlias("feels_like")
    private float feelsLike;

    @JsonAlias("temp_min")
    private float temperatureMin;

    @JsonAlias("temp_max")
    private float temperatureMax;

    @JsonAlias("pressure")
    private int pressure;

    @JsonAlias("humidity")
    private int humidity;

    @JsonAlias("sea_level")
    private int seaLevel;

    @JsonAlias("grnd_level")
    private int groundLevel;
}
