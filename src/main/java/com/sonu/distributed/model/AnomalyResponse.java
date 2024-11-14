package com.sonu.distributed.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Builder
@Getter
public class AnomalyResponse {
    private Map<Long,WeatherAnomalyStatistics> map;
    private Long totalMessageCount;
    private Long processedMessageCount;
    private float completedPercentage;
}
