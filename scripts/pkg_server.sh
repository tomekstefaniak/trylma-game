#!/bin/bash

# cd trylma || exit 1
# mvn package -pl server

cd trylma/server
mvn clean install
cd ..
cd ..