#!/bin/bash

if [ "${1}" == "" ]; then
    echo "Usage: $(basename $0) <number>"
    exit 1
fi

. $(dirname $0)/env.sh

docker run -it -h sandbox${1} -v $(pwd):/scripts -e SPARK_DIR="${SPARK_DIR_DOCKER}" maguowei/spark /scripts/run_worker_node.sh
