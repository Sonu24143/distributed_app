package com.sonu.distributed.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class DayTimeStatistics {
    private long sunrise;
    private long sunset;
    private String pod;
}
