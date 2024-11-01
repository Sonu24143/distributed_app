package com.sonu.distributed.model.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "wind_stats")
@Builder
@Data
@ToString()
public class WindStatsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "weather_stats_id", nullable = false)
    @ToString.Exclude
    private WeatherStatsEntity weatherStatsEntity;

    @Column(name = "speed")
    private float speed;

    @Column(name = "degree")
    private float degree;

    @Column(name = "gust")
    private float gust;
}
