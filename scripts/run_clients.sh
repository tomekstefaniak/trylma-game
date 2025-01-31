#!/bin/bash

cd trylma/client || exit 1

for i in $(seq 1 $1); do
    nickname="Player$i"
    mvn javafx:run &
done

wait
