DROP DATABASE spotitube;
CREATE DATABASE IF NOT EXISTS spotitube;
create table tracks
(
  id               int auto_increment,
  title            varchar(250) default 'NULL' null,
  performer        varchar(250) default 'NULL' null,
  duration         int default 'NULL'          null,
  album            varchar(250) default 'NULL' null,
  playcount        int default 'NULL'          null,
  publication_date date default 'NULL'         null,
  description      varchar(250) default 'NULL' null,
  constraint tracks_id_uindex
  unique (id)
);

alter table tracks
  add primary key (id);

create table users
(
  id       int auto_increment,
  user     varchar(250) default 'NULL' null,
  password varchar(250) default 'NULL' null,
  token    varchar(250) default 'NULL' null,
  constraint users_id_uindex
  unique (id),
  constraint users_token_uindex
  unique (token)
);

alter table users
  add primary key (id);

create table playlists
(
  name     varchar(250) default 'NULL' null,
  id       int auto_increment,
  owner_id int                         not null,
  constraint playlists_id_uindex
  unique (id),
  constraint playlists_users_id_fk
  foreign key (owner_id) references users (id)
);

alter table playlists
  add primary key (id);

create table playlist_track
(
  playlist_id       int                    not null,
  track_id          int                    not null,
  offline_available tinyint(1) default '0' null,
  constraint playlist_track_playlists_name_fk
  foreign key (playlist_id) references playlists (id)
    on delete cascade,
  constraint playlist_track_tracks_id_fk
  foreign key (track_id) references tracks (id)
);


INSERT INTO users(user, password) VALUES ('maarten', 'password');
INSERT INTO users(user, password) VALUES ('hello', 'world');
INSERT INTO tracks (title, performer, duration, album, playcount, publication_date, description) VALUES ('summerjam', 'spotify', 120, 'mio', 6, '2014-01-17', 'Lekker nummertje');
INSERT INTO tracks (title, performer, duration, album, playcount, publication_date, description) VALUES ('Turn Down For What', 'DJ Snake', 50, 'youtube', 900, '2015-01-31', 'top nummer');