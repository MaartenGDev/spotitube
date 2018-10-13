FROM tomee:8-jre-7.1.0-webprofile

RUN ["rm", "-rf", "/usr/local/tomee/webapps/ROOT"]
COPY target/spotitube.war /usr/local/tomee/webapps/ROOT.war

EXPOSE 8080