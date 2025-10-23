--liquibase formatted sql

--changeset b0nbun:1
create table office (
  id serial primary key,
  name varchar(255) not null,
  map bytea
);

create type audio_equipment_state as enum ('absent', 'headset', 'speakers');

create table workplace (
    id serial primary key,
    monitors int not null,
    audio_equipment audio_equipment_state not null,
    office_id int references office(id)
);

create table meeting_room (
    id serial primary key,
    office_id int references office(id),
    remote_available boolean not null,
    capacity integer not null
);

create table userset (
    id serial primary key,
    name varchar(255) not null
);

create table userset_office_rel (
    id serial primary key,
    userset_id int references userset(id),
    office_id int references office(id)
);

create type user_role as enum ('admin', 'supervisor', 'regular', 'infrequent');
create cast (character varying as user_role) with inout as implicit;

create table users (
    id serial primary key,
    name varchar(255) unique not null check (name <> ''),
    password_hash varchar(60) not null, -- bcrypt
    role user_role not null
);

create table user_userset_rel (
    id serial primary key,
    user_id int references users(id),
    userset_id int references userset(id)
);

create table workplace_booking (
    id serial primary key,
    workplace_id int references workplace(id),
    user_id int references users(id),
    booked_date date
);

create table meeting_booking (
    id serial primary key,
    user_id int references users(id),
    meeting_room_id int references meeting_room(id),
    start_time timestamp,
    end_time timestamp
);