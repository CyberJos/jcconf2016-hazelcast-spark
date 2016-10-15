#!/bin/bash

. $(dirname $0)/env.sh

${SPARK_DIR_LOCAL}/sbin/stop-master.sh
