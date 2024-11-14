package com.sonu.distributed.processor;

import com.sonu.distributed.config.qualifier.WordCountProcessorStreamBuilderConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class WordCountProcessor {
    private static final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    public void buildPipeline(@WordCountProcessorStreamBuilderConfig StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder.stream("test-input-stream-topic", Consumed.with(STRING_SERDE, STRING_SERDE));
        KTable<String, Long> wordCount = messageStream
                .mapValues((ValueMapper<String, String>) String::toLowerCase)
                .flatMapValues(value -> Arrays.asList(value.split("\\W+")))
                .groupBy((key, word) -> word, Grouped.with(STRING_SERDE, STRING_SERDE))
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("word-count").withValueSerde(Serdes.Long()));

        //wordCount.toStream().to("test-output-stream-topic", Produced.with(STRING_SERDE, Serdes.Long()));
    }
}
