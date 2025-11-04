--liquibase formatted sql

--changeset b0nbun:1
create table office (
  id serial primary key,
  name varchar(255) unique not null check (name <> ''),
  map bytea
);

create type audio_equipment_state as enum ('absent', 'headset', 'speakers');
create cast (character varying as audio_equipment_state) with inout as implicit;  

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

create table groups (
    id serial primary key,
    name varchar(255) unique not null check (name <> '')
);

create table groups_office_rel (
    id serial primary key,
    groups_id int references groups(id),
    office_id int references office(id),
    unique (groups_id, office_id)
);

create type user_role as enum ('admin', 'supervisor', 'regular', 'infrequent');
create cast (character varying as user_role) with inout as implicit;

create table users (
    id serial primary key,
    name varchar(255) unique not null check (name <> ''),
    password_hash varchar(60) not null, -- bcrypt
    role user_role not null
);

create table user_groups_rel (
    id serial primary key,
    user_id int references users(id),
    groups_id int references groups(id),
    unique (user_id, groups_id)
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