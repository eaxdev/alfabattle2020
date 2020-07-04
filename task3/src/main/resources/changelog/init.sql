create table if not exists branches
(
    id integer not null
        constraint branches_pk
            primary key,
    title varchar,
    lon numeric,
    lat numeric,
    address varchar
);

create table if not exists queue_log
(
    data date,
    start_time_of_wait time,
    end_time_of_wait time,
    end_time_of_service time,
    branches_id integer
        constraint queue_log_fk
            references branches,
    id serial not null
        constraint queue_log_pk
            primary key
);