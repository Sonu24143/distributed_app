package com.sonu.distributed.service;

import com.sonu.distributed.model.entity.WeatherStatsEntity;

public interface WeatherStatsService {
    WeatherStatsEntity create(WeatherStatsEntity entity);

    Long findLatestTimestamp();

    WeatherStatsEntity findForecastData(long timestamp);
}
