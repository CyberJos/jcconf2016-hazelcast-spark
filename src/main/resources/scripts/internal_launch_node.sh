#!/bin/bash

for (( i=1; i<=5; i=i+1 )); do
    if [ "$(grep sandbox${i} /etc/hosts)" == "" ]; then
        j=${i}
        if [ "${masterDocker}" == "1" ]; then
            let j=i+1
        fi   
        line="172.17.0.${j} sandbox${i}"
        echo "Append ${line} into /etc/hosts"
        echo ${line} >> /etc/hosts
    fi
done

username=$(who)
if [ "${username}" != "" ]; then
    username=$(whoami)
fi

if [ "$1" == "master" ]; then
    ${SPARK_DIR}/sbin/start-master.sh
    sleep 1
    tail -f ${SPARK_DIR}/logs/spark-${username}-org.apache.spark.deploy.master.Master-1-$(hostname).out -n 50
elif [ "$1" == "worker" ]; then
    ${SPARK_DIR}/sbin/start-slave.sh spark://sandbox1:7077
    tail -f ${SPARK_DIR}/logs/* -n 50
else
    echo "Usage: $(basename $0) [master|worker]"
    exit 1
fi

