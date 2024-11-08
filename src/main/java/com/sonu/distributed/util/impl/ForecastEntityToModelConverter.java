package com.sonu.distributed.util.impl;

import com.sonu.distributed.model.*;
import com.sonu.distributed.model.entity.WeatherStatsEntity;
import com.sonu.distributed.util.Converter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ForecastEntityToModelConverter implements Converter<WeatherStatsEntity, ForecastWeatherStatistics> {
    @Override
    public ForecastWeatherStatistics convert(WeatherStatsEntity data) {
        if( data == null) {
            return new ForecastWeatherStatistics();
        }
        List<Weather> weathers = data.getWeatherDescriptionEntities().stream()
                .map( e -> new Weather(e.getType(), e.getDescription())).toList();
        WeatherStatistics weatherStatistics = new WeatherStatistics(data.getTemperature(), data.getFeelsLike(), data.getTemperatureMin(), data.getTemperatureMax(), data.getPressure(), data.getHumidity(), data.getSeaLevel(), data.getGroundLevel());
        WindStatistics windStatistics = new WindStatistics(data.getWindStatsEntity().getSpeed(), data.getWindStatsEntity().getDegree(), data.getWindStatsEntity().getGust());
        RainStatistics rainStatistics = new RainStatistics(data.getRainMm());
        CloudStatistics cloudStatistics = new CloudStatistics(data.getCloudiness());
        DayTimeStatistics dayTimeStatistics = new DayTimeStatistics();
        return new ForecastWeatherStatistics(weathers, weatherStatistics, data.getVisibility(), windStatistics, rainStatistics, cloudStatistics, dayTimeStatistics, data.getCollectionTime(), data.getPrecipitation());
    }
}
