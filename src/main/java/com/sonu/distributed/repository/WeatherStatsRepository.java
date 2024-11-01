package com.sonu.distributed.repository;

import com.sonu.distributed.model.entity.WeatherStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherStatsRepository extends JpaRepository<WeatherStatsEntity, String> {
    @Query("select max(timestamp) from WeatherStatsEntity")
    Long findMaxTimestamp();
}
