package com.sonu.distributed.repository;

import com.sonu.distributed.model.entity.WeatherStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherStatsRepository extends JpaRepository<WeatherStatsEntity, String> {
    @Query(value = "select max(collection_time) from weather_stats", nativeQuery = true)
    Long findMaxTimestamp();

    WeatherStatsEntity findByCollectionTimeEquals(long time);
}
