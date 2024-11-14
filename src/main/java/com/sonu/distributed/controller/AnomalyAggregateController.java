package com.sonu.distributed.controller;

import com.sonu.distributed.common.Constants;
import com.sonu.distributed.config.qualifier.AnomalyAggregationProcessorStreamBuilderConfig;
import com.sonu.distributed.model.AnomalyResponse;
import com.sonu.distributed.model.WeatherAnomalyStatistics;
import com.sonu.distributed.util.LogElapsedTime;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class AnomalyAggregateController {
    @Autowired
    @AnomalyAggregationProcessorStreamBuilderConfig private StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    @Value("${message.duplication.count}")
    private Integer messageDuplicationCount;

    @LogElapsedTime
    @GetMapping("/anomaly")
    public AnomalyResponse getAnomaly() {
        Map<Long,WeatherAnomalyStatistics> result = new HashMap<>();
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        ReadOnlyWindowStore<Windowed<Long>, WeatherAnomalyStatistics> windowStore = kafkaStreams.store(StoreQueryParameters.fromNameAndType(Constants.ANOMALY_DATA_STORE_NAME, QueryableStoreTypes.windowStore()));
        Instant from = Instant.ofEpochMilli(0L);
        Instant to = Instant.now();
        KeyValueIterator<Windowed<Windowed<Long>>, WeatherAnomalyStatistics> iterator = windowStore.fetchAll(from, to);
        long counter = 0;
        long processedCount = 0;
        while (iterator.hasNext()) {
            KeyValue<Windowed<Windowed<Long>>, WeatherAnomalyStatistics> keyValue = iterator.next();
            log.error("Adding to result [{}/{}]", keyValue.key.window(), keyValue.value);
            result.put(keyValue.key.window().start(), keyValue.value);
            processedCount += keyValue.value.getAggregatedRecordCount();
            counter++;
        }

        long totalCount = messageDuplicationCount * 30 * counter;
        return AnomalyResponse.builder()
                .map(result)
                .totalMessageCount(totalCount)
                .processedMessageCount(processedCount)
                .completedPercentage((float) processedCount / (totalCount / 100.f))
                .build();
    }
}
