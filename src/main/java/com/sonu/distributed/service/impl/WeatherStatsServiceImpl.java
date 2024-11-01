package com.sonu.distributed.service.impl;

import com.sonu.distributed.model.entity.WeatherStatsEntity;
import com.sonu.distributed.repository.WeatherStatsRepository;
import com.sonu.distributed.service.WeatherStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
