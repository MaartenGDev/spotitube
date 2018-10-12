FROM tomee:8-jre-7.1.0-webprofile

ADD target/spotitube.war /usr/local/tomee/webapps/spotitube.war

EXPOSE 8080