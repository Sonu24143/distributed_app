package com.sonu.distributed.scheduler;

import com.sonu.distributed.model.ForecastWeatherStatistics;
import com.sonu.distributed.model.ForecastWeatherStatisticsCollection;
import com.sonu.distributed.model.entity.WeatherStatsEntity;
import com.sonu.distributed.service.WeatherService;
import com.sonu.distributed.service.WeatherStatsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class WeatherForecastDataLoader {
    @Value("${openweather.base.lat}")
    private float lattitude;

    @Value("${openweather.base.lon}")
    private float longitude;
    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherStatsService weatherStatsService;

    @Scheduled(initialDelay = 1000, fixedRate = 86400000)
    public void execute() {
        log.info("Starting forecast data load process...");
        long start = System.currentTimeMillis();
        try {
            Long latestTimestamp = weatherStatsService.findLatestTimestamp();
            log.info("Latest forecast data in DB is at [{}].", latestTimestamp);
            ForecastWeatherStatisticsCollection forecastWeatherStatisticsCollection = weatherService.getForecastedWeather(lattitude, longitude);
            for(ForecastWeatherStatistics forecastWeatherStatistics: forecastWeatherStatisticsCollection.getList()) {
                if(latestTimestamp == null || (forecastWeatherStatistics.getTimestamp() * 1000) > latestTimestamp) {
                    log.info("Fetched forecast [{}].", forecastWeatherStatistics);
                    // Since stats are generated at intervals of 3 hours, but app expects information at hour granularity
                    // we synthetically generate 2 filler entities with same parameter value except the time stamp which
                    // is updated as n, n+1 hour, n+2 hour for forecast of nth hour.
                    List<WeatherStatsEntity> weatherStatsEntities = forecastWeatherStatistics.convert();
                    for(WeatherStatsEntity weatherStatsEntity: weatherStatsEntities) {
                        log.info("Converted to entity [{}]", weatherStatsEntity);
                        weatherStatsService.create(weatherStatsEntity);
                    }
                }
            }
        } catch (Exception e) {
            log.error("DB op failed, due to ",e);
        }
        log.info("Finished loading forecast data in [{}] ms.", (System.currentTimeMillis() - start));
    }
}
