#!/bin/sh


PATH="/public/kn/spark-2.4.4-bin-without-hadoop/bin:/public/kn/spark-2.4.4-bin-without-hadoop/sbin:/public/kn/hadoop-3.1.2/sbin:/public/kn/hadoop-3.1.2/bin:$PATH"
HADOOP_HOME="/public/kn/hadoop-3.1.2"
SPARK_HOME="/public/kn/spark-2.4.4-bin-without-hadoop"
RES_DIR="$(dirname "$0")"
RES_DIR="$(realpath "$RES_DIR"/..)"


"$RES_DIR/scripts/start-hadoop.sh"

echo -n 'Starting master ... '
start-master.sh
echo ok
echo -n 'Starting worker ... '
start-slave.sh "spark://$(hostname):7077"
echo ok