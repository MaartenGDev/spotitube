CREATE DATABASE IF NOT EXISTS spotitube;

create table if not exists tracks
(
	id int auto_increment,
	title varchar(250) default null null,
	performer varchar(250) default null null,
	duration int default null null,
	album varchar(250) default null null,
	playcount int default null null,
	publicationDate date default null null,
	description varchar(250) default null null,
	offlineAvailable tinyint(1) default '0' null,
	constraint tracks_id_uindex
		unique (id)
)
;

alter table tracks
	add primary key (id)
;

create table if not exists users
(
	id int auto_increment,
	user varchar(250) default null null,
	password varchar(250) default null null,
	constraint users_id_uindex
		unique (id)
)
;

alter table users
	add primary key (id)
;

create table if not exists playlists
(
	name varchar(250) NOT NULL ,
	id int auto_increment,
	owner_id int not null,
	constraint playlists_id_uindex
		unique (id),
	constraint playlists_users_id_fk
		foreign key (owner_id) references users (id)
)
;

alter table playlists
	add primary key (id)
;

create table if not exists playlist_track
(
	playlist_id int not null,
	track_id int not null,
	constraint playlist_track_playlists_name_fk
		foreign key (playlist_id) references playlists (id)
			on delete cascade,
	constraint playlist_track_tracks_id_fk
		foreign key (track_id) references tracks (id)
)
;


INSERT INTO spotitube.users(user, password) VALUES ('maarten', 'password')

INSERT INTO spotitube.tracks (title, performer, duration, album, playcount, publicationDate, description, offlineAvailable) VALUES ('summerjam', 'spotify', 120, 'mio', 6, '2014-01-17', 'Lekker nummertje', 1);
INSERT INTO spotitube.tracks (title, performer, duration, album, playcount, publicationDate, description, offlineAvailable) VALUES ('Turn Down For What', 'DJ Snake', 50, 'youtube', 900, '2015-01-31', 'top nummer', 0);