package com.sonu.distributed.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ForecastWeatherStatisticsCollection {
    private List<ForecastWeatherStatistics> list;
}
