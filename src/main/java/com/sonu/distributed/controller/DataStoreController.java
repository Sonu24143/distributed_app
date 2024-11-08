package com.sonu.distributed.controller;

import com.sonu.distributed.config.qualifier.WordCountProcessorStreamBuilderConfig;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.apache.kafka.streams.StoreQueryParameters.fromNameAndType;

@Slf4j
@RestController
public class DataStoreController {
    @Autowired
    @WordCountProcessorStreamBuilderConfig private StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    @Autowired
    private KafkaProducer<Long, String> kafkaProducer;

    @PostConstruct
    private void init() {
        try {
            kafkaProducer.send(new ProducerRecord<>( "", System.currentTimeMillis(), "Test message"));
            kafkaProducer.flush();
        } finally {
            kafkaProducer.close();
        }
    }

    @GetMapping("/count/{word}")
    public long getCount(@PathVariable String word) throws ChangeSetPersister.NotFoundException {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        Function<ReadOnlyKeyValueStore<String, Long>, KeyValueIterator<String, Long>> keyValueIterator = ReadOnlyKeyValueStore::all;
        final ReadOnlyKeyValueStore<String, Long> store =
                kafkaStreams.store(fromNameAndType("word-count", QueryableStoreTypes.keyValueStore()));
        if(store == null || store.get(word) == null) {
            throw new ResourceNotFoundException(word + " -> is unavailable in data store.");
        } else {
            return store.get(word);
        }
    }

    @GetMapping("/count")
    public Map<String, Long> getCount() {
        Map<String, Long> response = new HashMap<>();
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        Function<ReadOnlyKeyValueStore<String, Long>, KeyValueIterator<String, Long>> keyValueIterator = ReadOnlyKeyValueStore::all;
        final ReadOnlyKeyValueStore<String, Long> store = kafkaStreams.store(fromNameAndType("word-count", QueryableStoreTypes.keyValueStore()));
        final KeyValueIterator<String, Long> range = keyValueIterator.apply(store);
        while (range.hasNext()) {
            final org.apache.kafka.streams.KeyValue<String, Long> next = range.next();
            response.put(next.key, next.value);
            log.info("Key = [{}], value = [{}].", next.key, next.value);
        }
        return response;
    }
}
