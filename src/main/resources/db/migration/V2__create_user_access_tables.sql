create table if not exists weather_anamoly_app.role_details (
    id varchar(255) not null,
    name varchar(255) not null,
    enabled boolean not null,
    primary key (id, name)
);

create table if not exists weather_anamoly_app.user_details (
    id varchar(255) not null,
    name varchar(255),
    username varchar(255) not null,
    password varchar(255) not null,
    enabled boolean not null default false,
    primary key(id, username)
);

create table if not exists weather_anamoly_app.user_role_mapping (
    id varchar(255) not null,
    user_id varchar(255) not null,
    role_id varchar(255) not null,
    primary key(id, user_id, role_id),
    foreign key (user_id) references user_details(id),
    foreign key (role_id) references role_details(id)
);