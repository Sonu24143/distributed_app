package com.sonu.distributed.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class WindStatistics {
    @JsonAlias("speed")
    private float speed;

    @JsonAlias("deg")
    private float degree;

    @JsonAlias("gust")
    private float gust;
}
