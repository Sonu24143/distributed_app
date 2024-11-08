package com.sonu.distributed.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

import java.util.Set;

@Entity
@Table(name = "weather_stats")
@NoArgsConstructor
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

    @Column(name = "collection_time", updatable = false)
    private Long collectionTime;

    @Column(name = "created_time", updatable = false)
    private Long createdTime;

    @OneToMany(mappedBy = "weatherStatsEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<WeatherDescriptionEntity> weatherDescriptionEntities;

    @OneToOne(mappedBy = "weatherStatsEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private WindStatsEntity windStatsEntity;
}
