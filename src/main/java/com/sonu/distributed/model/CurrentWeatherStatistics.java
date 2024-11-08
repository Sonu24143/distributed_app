package com.sonu.distributed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class CurrentWeatherStatistics extends DetailedWeatherStatistics {
    @JsonAlias("dt")
    @Setter
    private long timestamp;

    @JsonAlias("timezone")
    private long offset;
}
