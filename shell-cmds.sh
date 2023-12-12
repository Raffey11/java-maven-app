#!/usr/bin/env bash

docker-compose -f docker-compose.yaml down >> /dev/null

docker-compose -f docker-compose.yaml up --detach