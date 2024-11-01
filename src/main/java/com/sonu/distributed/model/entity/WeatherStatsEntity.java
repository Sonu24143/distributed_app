package com.sonu.distributed.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "weather_stats")
@Builder
@Data
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class WeatherStatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private String id;

    @Column(name = "temprature")
    private Float temperature;

    @Column(name = "feels_like")
    private Float feelsLike;

    @Column(name = "temprature_min")
    private Float temperatureMin;

    @Column(name = "temprature_max")
    private Float temperatureMax;

    private Float pressure;

    private Float humidity;

    @Column(name = "sea_level")
    private Float seaLevel;

    @Column(name = "ground_level")
    private Float groundLevel;

    @Column(name = "rain_mm")
    private Float rainMm;

    @Column(name = "cloudiness_percentage")
    private Float cloudiness;

    @Column(name = "percentage_of_precipitation")
    private Float precipitation;

    private Integer visibility;

    private Long timestamp;

    @Column(name = "created_time")
    private Long createdTime;

    @OneToMany(mappedBy = "weatherStatsEntity", cascade = CascadeType.ALL)
    private Set<WeatherDescriptionEntity> weatherDescriptionEntities;

    @OneToOne(mappedBy = "weatherStatsEntity", cascade = CascadeType.ALL)
    private WindStatsEntity windStatsEntity;
}
