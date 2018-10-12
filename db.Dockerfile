FROM mariadb:10.3.10-bionic

ADD db/structure.sql /docker-entrypoint-initdb.d/structure.sql

EXPOSE 3306