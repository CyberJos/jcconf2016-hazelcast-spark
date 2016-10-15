#!/bin/bash

. $(dirname $0)/env.sh

docker run -it -h sandbox1 -p 7077:7077 -p 8080:8080 -v $(pwd):/scripts -e SPARK_DIR="${SPARK_DIR_DOCKER}" -e masterDocker="1" maguowei/spark /scripts/run_master_node.sh
