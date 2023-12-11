#!/usr/bin/env bash

docker stop java-maven-app
docker stop postgres
docker rm java-maven-app
docker rm postgres

docker-compose -f docker-compose.yaml up --detach