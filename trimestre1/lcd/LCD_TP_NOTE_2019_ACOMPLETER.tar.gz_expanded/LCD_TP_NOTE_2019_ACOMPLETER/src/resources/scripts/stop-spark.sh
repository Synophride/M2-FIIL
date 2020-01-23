#!/bin/sh

PATH="/public/kn/spark-2.3.2-bin-without-hadoop/bin:/public/kn/spark-2.3.2-bin-without-hadoop/sbin:/public/kn/hadoop-2.8.5/sbin:/public/kn/hadoop-2.8.5/bin:$PATH"
HADOOP_HOME="/public/kn/hadoop-2.8.5"
SPARK_HOME="/public/kn/spark-2.3.2-bin-without-hadoop"

RES_DIR="$(dirname "$0")"
RES_DIR="$(realpath "$RES_DIR"/..)"

stop-slave.sh
stop-master.sh

stop-dfs.sh || true
pkill -f 'java .*org[.]apache[.]hadoop' || true

rm -rf /tmp/hadoop*
