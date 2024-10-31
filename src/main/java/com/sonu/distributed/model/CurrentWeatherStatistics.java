package com.sonu.distributed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(callSuper = true)
public class CurrentWeatherStatistics extends DetailedWeatherStatistics {
    @JsonAlias("dt")
    private long timestamp;

    @JsonAlias("timezone")
    private long offset;
}
