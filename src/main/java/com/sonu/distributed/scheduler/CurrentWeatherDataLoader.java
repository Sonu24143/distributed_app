package com.sonu.distributed.scheduler;

import com.sonu.distributed.model.CurrentWeatherStatistics;
import com.sonu.distributed.service.WeatherService;
import com.sonu.distributed.util.LogElapsedTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class CurrentWeatherDataLoader {
    @Value("${message.duplication.count}")
    private int duplicationCount;

    @Value("${openweather.base.lat}")
    private float lattitude;

    @Value("${openweather.base.lon}")
    private float longitude;

    @Value("${input.topic.name}")
    private String topicName;

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private KafkaProducer<String, Bytes> kafkaProducer;

    private final AtomicInteger maxRetries = new AtomicInteger(5);
    JsonSerializer<CurrentWeatherStatistics> jsonSerializer = new JsonSerializer<>();

    @LogElapsedTime
    @Scheduled(initialDelay = 1000, fixedDelay = 1100)
    public void execute() {
        long start = System.currentTimeMillis();
        if(maxRetries.get() <= 0) {
            log.warn("Retries exhausted, current weather data will not be loaded.");
            return;
        }
        try {
            CurrentWeatherStatistics currentWeatherStatistics = weatherService.getCurrentWeather(lattitude, longitude);
            log.info("Fetched current weather details [{}].", currentWeatherStatistics);
            String key = Long.toString(((currentWeatherStatistics.getTimestamp() - currentWeatherStatistics.getOffset()) * 1000));
            Bytes value = Bytes.wrap(jsonSerializer.serialize(topicName, currentWeatherStatistics));
            for(int i=0; i<duplicationCount; i++) {
                kafkaProducer.send(new ProducerRecord<>(topicName, key, value));
            }
            kafkaProducer.flush();
        } catch (Exception e) {
            log.error("Failed to load current weather data into stream.", e);
            maxRetries.decrementAndGet();
        }
        log.info("Published [{}] message in [{}] ms.", duplicationCount, (System.currentTimeMillis() - start));
    }
}
