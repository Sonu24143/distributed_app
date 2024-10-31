package com.sonu.distributed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DetailedWeatherStatistics {
    private List<Weather> weather;

    @JsonAlias("main")
    private WeatherStatistics weatherStatistics;

    private int visibility;

    @JsonAlias("wind")
    private WindStatistics windStatistics;

    @JsonAlias("rain")
    private RainStatistics rainStatistics;

    @JsonAlias("clouds")
    private CloudStatistics cloudStatistics;

    @JsonAlias("sys")
    private DayTimeStatistics dayTimeStatistics;
}
