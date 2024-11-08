insert into weather_anomaly_schema.role_details values (uuid(), 'ADMIN', true);
insert into weather_anomaly_schema.role_details values (uuid(), 'USER', true);

insert into weather_anomaly_schema.user_details values (uuid(), 'first_user', 'admin', '7c5d7f40-9de6-11ef-b8d3-48a56301da43', true);

insert into weather_anomaly_schema.user_role_mapping values (uuid(),
    (select id from weather_anomaly_schema.user_details where username = 'admin'),
    (select id from weather_anomaly_schema.role_details where name = 'ADMIN')
);