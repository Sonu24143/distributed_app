package com.sonu.distributed.model.serde;

import com.sonu.distributed.model.WeatherAnomalyStatistics;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

public class WeatherAnomalyStatisticsSerde implements Serde<WeatherAnomalyStatistics> {
    private final JsonSerializer<WeatherAnomalyStatistics> serializer = new JsonSerializer<>();
    private final JsonDeserializer<WeatherAnomalyStatistics> deserializer = new JsonDeserializer<>(WeatherAnomalyStatistics.class);
    private boolean isInit = false;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        serializer.configure(configs, isKey);
        deserializer.configure(configs, isKey);
    }

    @Override
    public void close() {
        serializer.close();
        deserializer.close();
    }

    @Override
    public Serializer<WeatherAnomalyStatistics> serializer() {
        return serializer;
    }

    @Override
    public Deserializer<WeatherAnomalyStatistics> deserializer() {
        if(!isInit) {
            isInit = true;
            deserializer.trustedPackages("*");
            deserializer.ignoreTypeHeaders();
        }
        return deserializer;
    }
}
