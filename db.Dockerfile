FROM mariadb:10.3.10-bionic

ADD db/db.sql /docker-entrypoint-initdb.d/db.sql

EXPOSE 3306