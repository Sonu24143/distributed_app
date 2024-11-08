package com.sonu.distributed.common;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public interface Constants {
    long HOUR_IN_MILLIS = 60 * 60 * 1000L;
    Serde<Long> LONG_SERDE = Serdes.Long();
}
