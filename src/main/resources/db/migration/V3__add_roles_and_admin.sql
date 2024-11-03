insert into weather_anamoly_app.role_details values (uuid(), 'ADMIN', true);
insert into weather_anamoly_app.role_details values (uuid(), 'USER', true);

insert into weather_anamoly_app.user_details values (uuid(), 'first_user', 'admin', 'admin', true);

insert into weather_anamoly_app.user_role_mapping values (uuid(),
    (select id from weather_anamoly_app.user_details where username = 'admin'),
    (select id from weather_anamoly_app.role_details where name = 'ADMIN')
);