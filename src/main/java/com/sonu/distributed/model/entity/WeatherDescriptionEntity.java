package com.sonu.distributed.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "weather_description")
@Builder
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WeatherDescriptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @ManyToOne
    @JoinColumn(name = "weather_stats_id", nullable = false)
    @ToString.Exclude
    private WeatherStatsEntity weatherStatsEntity;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;
}
