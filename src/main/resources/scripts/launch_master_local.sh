#!/bin/bash

. $(dirname $0)/env.sh

export SPARK_DIR=${SPARK_DIR_LOCAL}
export SPARK_MASTER_HOST=sandbox1

$(dirname $0)/run_master_node.sh
