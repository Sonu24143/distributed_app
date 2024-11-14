package com.sonu.distributed.processor;

import com.sonu.distributed.common.Constants;
import com.sonu.distributed.config.qualifier.AnomalyAggregationProcessorStreamBuilderConfig;
import com.sonu.distributed.model.WeatherAnomalyStatistics;
import com.sonu.distributed.model.serde.WeatherAnomalyStatisticsSerde;
import com.sonu.distributed.util.EpochUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class AnomalyAggregationProcessor {
    @Value("${anomaly.topic.name}")
    private String anomalyTopicName;

    private static final Long WINDOW_DURATION = 60000L;


    @Autowired
    public void buildPipeline(@AnomalyAggregationProcessorStreamBuilderConfig StreamsBuilder streamsBuilder) {
        KStream<Long, WeatherAnomalyStatistics> kStream = streamsBuilder.stream(anomalyTopicName, Consumed.with(Constants.LONG_SERDE, new WeatherAnomalyStatisticsSerde()).withOffsetResetPolicy(Topology.AutoOffsetReset.LATEST));
        KTable<Windowed<Long>, WeatherAnomalyStatistics> kTable = kStream.filter((key, value) -> (value.getCollectedTime() != null && value.getTemperature() != null))
                // group data by rounding off collected time based on duration of aggregation
                .map((key, value) -> new KeyValue<>(EpochUtils.roundOffToDuration(value.getCollectedTime(), WINDOW_DURATION), value))
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                .aggregate(WeatherAnomalyStatistics::new, (key, value, agg) -> agg.aggregateTemperatureDetails(value), Materialized.as(Constants.ANOMALY_DATA_STORE_NAME));
                /*.toStream()
                .to("anomaly-aggregated-topic", Produced.with(TIME_WINDOWED_SERDE, new WeatherAnomalyStatisticsSerde()));*/

    }
}
