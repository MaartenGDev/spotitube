# SpotiTube Project
[![Build Status](https://travis-ci.org/MaartenGDev/spotitube.svg?branch=master)](https://travis-ci.org/MaartenGDev/spotitube)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=me.maartendev%3Aspotitube&metric=alert_status)](https://sonarcloud.io/dashboard?id=me.maartendev%3Aspotitube)
 
## Local Development
1. Import as maven project
2. Run with IntelliJ

## Running the project
- `docker-compose up`
- This will start tomee with spotitube as root site on `localhost:9080`

## Tooling
- [Travis](https://travis-ci.org/MaartenGDev/spotitube/builds)
- [Sonarcloud](https://sonarcloud.io/dashboard?id=me.maartendev%3Aspotitube)
- AWS ECR
- AWS ECS
- AWS Route 53

# Production
This site is hosted on Amazon Web Services in docker containers. The site can be viewed [here](http://spotitube.maartendev.me).