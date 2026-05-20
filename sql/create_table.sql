create database playlistdb
default character set utf8mb4
default collate utf8mb4_unicode_ci;

use playlistdb;

create table user (
    user_id int auto_increment primary key,
    email varchar(100) not null unique,
    name varchar(50) not null
);

create table music (
    music_id int auto_increment primary key,
    title varchar(100) not null,
    artist varchar(100) not null,
    genre varchar(50),
    play_time varchar(10)
);

create table playlist (
    playlist_id int auto_increment primary key,
    user_id int not null,
    playlist_name varchar(100) not null,

    foreign key (user_id)
    references user(user_id)
    on delete cascade
);

create table playlist_music (
    playlist_id int not null,
    music_id int not null,
    primary key (playlist_id, music_id),

    foreign key (playlist_id)
    references playlist(playlist_id)
    on delete cascade,

    foreign key (music_id)
    references music(music_id)
    on delete cascade
);