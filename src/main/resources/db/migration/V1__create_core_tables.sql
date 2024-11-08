create table if not exists weather_anomaly_schema.weather_stats (
	id varchar(255) not null,
    temprature float,
    feels_like float,
    temprature_min float,
    temprature_max float,
    pressure float,
    humidity float,
    sea_level float,
    ground_level float,
    rain_mm float,
    cloudiness_percentage float,
    percentage_of_precipitation float,
    visibility int,
    collection_time bigint(13),
    created_time long,
    primary key (id, collection_time)
);

create table if not exists weather_anomaly_schema.weather_description (
	id varchar(255) not null,
    weather_stats_id varchar(255) not null,
    type text,
    description mediumtext,
    primary key(id),
    foreign key (weather_stats_id) references weather_stats(id)
);

create table if not exists weather_anomaly_schema.wind_stats (
	id varchar(255) not null,
    weather_stats_id varchar(255) not null,
    speed float,
    degree float,
    gust float,
    primary key(id),
    foreign key (weather_stats_id) references weather_stats(id)
);