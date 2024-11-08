package com.sonu.distributed.processor;

import com.sonu.distributed.common.Constants;
import com.sonu.distributed.config.qualifier.AnomalyProcessorStreamBuilderConfig;
import com.sonu.distributed.model.CurrentWeatherStatistics;
import com.sonu.distributed.model.ForecastWeatherStatistics;
import com.sonu.distributed.model.WeatherAnomalyStatistics;
import com.sonu.distributed.model.WeatherCorrelationData;
import com.sonu.distributed.model.entity.WeatherStatsEntity;
import com.sonu.distributed.service.WeatherStatsService;
import com.sonu.distributed.util.Converter;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import javax.inject.Named;

@Slf4j
@Component
public class AnomalyDataProcessor {
    @Value("${input.topic.name}")
    private String inputTopicName;

    @Value("${anomaly.topic.name}")
    private String anomalyTopicName;

    @Autowired
    private WeatherStatsService weatherStatsService;

    @Autowired
    private Converter<WeatherStatsEntity, ForecastWeatherStatistics> converter;

    private static final JsonSerializer<CurrentWeatherStatistics> CURRENT_WEATHER_STATISTICS_JSON_SERIALIZER = new JsonSerializer<>();
    private static final JsonDeserializer<CurrentWeatherStatistics> CURRENT_WEATHER_STATISTICS_JSON_DESERIALIZER = new JsonDeserializer<>(CurrentWeatherStatistics.class);
    private static final Serde<CurrentWeatherStatistics> CURRENT_WEATHER_STATISTICS_SERDE = Serdes.serdeFrom(CURRENT_WEATHER_STATISTICS_JSON_SERIALIZER, CURRENT_WEATHER_STATISTICS_JSON_DESERIALIZER);

    private static final JsonSerializer<WeatherAnomalyStatistics> WEATHER_ANOMALY_STATISTICS_JSON_SERIALIZER = new JsonSerializer<>();
    private static final JsonDeserializer<WeatherAnomalyStatistics> WEATHER_ANOMALY_STATISTICS_JSON_DESERIALIZER = new JsonDeserializer<>(WeatherAnomalyStatistics.class);
    private static final Serde<WeatherAnomalyStatistics> WEATHER_ANOMALY_STATISTICS_SERDE = Serdes.serdeFrom(WEATHER_ANOMALY_STATISTICS_JSON_SERIALIZER, WEATHER_ANOMALY_STATISTICS_JSON_DESERIALIZER);

    @Autowired
    public void buildPipeline(@AnomalyProcessorStreamBuilderConfig StreamsBuilder streamsBuilder) {
        streamsBuilder.stream(inputTopicName, Consumed.with(Constants.LONG_SERDE, CURRENT_WEATHER_STATISTICS_SERDE).withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST))
                .mapValues((key, value) -> {
                    WeatherStatsEntity weatherStatsEntity = null;
                    log.debug("Computing anomaly for weather [{}].", value);
                    try {
                        weatherStatsEntity = weatherStatsService.findForecastData(value.getTimestamp());
                        ForecastWeatherStatistics forecastWeatherStatistics = converter.convert(weatherStatsEntity);
                        return new WeatherCorrelationData(forecastWeatherStatistics, value);
                    } catch (Exception e) {
                        log.error("Issue due to invalid forecast data / current weather data.\ntimestamp [{}], forecast [{}].", value.getTimestamp(), weatherStatsEntity);
                        return new WeatherCorrelationData();
                    }

                })
                .mapValues((key, value) -> {
                    WeatherAnomalyStatistics weatherAnomalyStatistics = new WeatherAnomalyStatistics();
                    return weatherAnomalyStatistics.apply(value);
                })
                .to(anomalyTopicName, Produced.with(Constants.LONG_SERDE, WEATHER_ANOMALY_STATISTICS_SERDE));

    }
}
