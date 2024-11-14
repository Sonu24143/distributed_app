package com.sonu.distributed.config;

import com.sonu.distributed.common.Constants;
import com.sonu.distributed.config.qualifier.AnomalyAggregationProcessorStreamBuilderConfig;
import com.sonu.distributed.config.qualifier.AnomalyProcessorStreamBuilderConfig;
import com.sonu.distributed.config.qualifier.WordCountProcessorStreamBuilderConfig;
import com.sonu.distributed.exceptions.SkipInvalidMessageHandler;
import com.sonu.distributed.model.serde.WeatherAnomalyStatisticsSerde;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.streams.StreamsConfig.*;

@EnableKafka
@Configuration
public class KafkaConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:2181");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "group-id");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    KafkaProducer<Long, Bytes> getKafkaProducer() {
        Map<String, Object> map = new HashMap<>();
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BytesSerializer.class);
        map.put(ProducerConfig.ACKS_CONFIG, "all");
        return new KafkaProducer<>(map);
    }

    @Bean
    KafkaProducer<Long, String> getInitKafkaProducer() {
        Map<String, Object> map = new HashMap<>();
        map.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        map.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        map.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BytesSerializer.class);
        map.put(ProducerConfig.ACKS_CONFIG, "all");
        return new KafkaProducer<>(map);
    }

    @Bean(name = Constants.ANOMALY_PROCESSOR_CONFIG)
    @AnomalyProcessorStreamBuilderConfig
    StreamsBuilderFactoryBean anomalyProcessorStreamConfig() {
        Map<String, Object> props = getCommonKafkaStreamProps();
        props.put(APPLICATION_ID_CONFIG, "anomaly-processor");
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Bytes().getClass().getName());
        props.put(COMMIT_INTERVAL_MS_CONFIG, 1000);
        props.put(NUM_STREAM_THREADS_CONFIG, 4);
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
    }

    @Bean(name = Constants.ANOMALY_AGGREGATION_PROCESSOR_CONFIG)
    @AnomalyAggregationProcessorStreamBuilderConfig
    StreamsBuilderFactoryBean anomalyAggregationProcessorStreamConfig() {
        Map<String, Object> props = getCommonKafkaStreamProps();
        props.put(APPLICATION_ID_CONFIG, "aggregation-processor");
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, WeatherAnomalyStatisticsSerde.class);
        props.put(COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        props.put(NUM_STREAM_THREADS_CONFIG, 4);
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
    }

    @Bean(name = Constants.WORD_COUNT_PROCESSOR_CONFIG)
    @WordCountProcessorStreamBuilderConfig
    StreamsBuilderFactoryBean wordCountProcessorStreamConfig() {
        Map<String, Object> props = getCommonKafkaStreamProps();
        props.put(APPLICATION_ID_CONFIG, "word-count-processor");
        props.put(DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(COMMIT_INTERVAL_MS_CONFIG, 10 * 1000);
        props.put(STATESTORE_CACHE_MAX_BYTES_CONFIG, 0);
        return new StreamsBuilderFactoryBean(new KafkaStreamsConfiguration(props));
    }

    private Map<String, Object> getCommonKafkaStreamProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(STATE_DIR_CONFIG, "/Users/sonuparekaden/kafka-tmp");
        props.put(DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, SkipInvalidMessageHandler.class);
        return props;
    }
}
