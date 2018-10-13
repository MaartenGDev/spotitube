FROM tomee:8-jre-7.1.0-webprofile

ADD target/spotitube.war /usr/local/tomee/webapps/ROOT.war
ADD target/spotitube /usr/local/tomee/webapps/ROOT

EXPOSE 8080