package com.sonu.distributed.service.impl;

import com.sonu.distributed.common.Constants;
import com.sonu.distributed.model.entity.WeatherStatsEntity;
import com.sonu.distributed.repository.WeatherStatsRepository;
import com.sonu.distributed.service.WeatherStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WeatherStatsServiceImpl implements WeatherStatsService {
    @Autowired
    private WeatherStatsRepository weatherStatsRepository;

    @Override
    public WeatherStatsEntity create(WeatherStatsEntity entity) {
        return weatherStatsRepository.save(entity);
    }

    @Override
    public Long findLatestTimestamp() {
        return weatherStatsRepository.findMaxTimestamp();
    }

    @Override
    public WeatherStatsEntity findForecastData(long timestamp) {
        long time = timestamp - (timestamp % Constants.HOUR_IN_MILLIS);
        log.debug("Current weather: time [{}], belongs to hour [{}].", timestamp, time);
        WeatherStatsEntity weatherStatsEntity = getEntityByTimestamp(time);
        log.debug("Fetched forecast: time [{}], forecast [{}].", time, weatherStatsEntity);
        return weatherStatsEntity;
    }

    @Cacheable("forecast_data")
    private WeatherStatsEntity getEntityByTimestamp(long timestamp) {
        return weatherStatsRepository.findByCollectionTimeEquals(timestamp);
    }
}
