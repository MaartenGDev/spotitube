create table tracks
(
  id               int auto_increment,
  title            varchar(250) not null,
  performer        varchar(250) not null,
  duration         int default 0      null,
  album            varchar(250) not null,
  playcount        int default 0 null,
  publication_date date not null,
  description      varchar(250) not null,
  constraint tracks_id_uindex
  unique (id)
);

alter table tracks
  add primary key (id);

create table users
(
  id       int auto_increment,
  user     varchar(250) not null,
  password varchar(250) not null,
  token    varchar(250) not null,
  constraint users_id_uindex
  unique (id),
  constraint users_token_uindex
  unique (user,token)
);

alter table users
  add primary key (id);

create table playlists
(
  name     varchar(250) not null,
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


INSERT INTO users(user, password, token) VALUES ('maarten', 'password', 'aaa-bbb');
