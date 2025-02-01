#!/bin/bash

# java -jar trylma/server/target/server-1.0-SNAPSHOT.jar $1 $2 $3

cd trylma/server
mvn spring-boot:run -Dspring-boot.run.arguments="$1 $2 $3"
cd ..
cd ..
