#!/bin/sh

PATH="/public/kn/hadoop-3.1.2/sbin:/public/kn/hadoop-3.1.2/bin:$PATH"
HADOOP_HOME="/public/kn/hadoop-3.1.2"
RES_DIR="$(dirname "$0")"
RES_DIR="$(realpath "$RES_DIR"/..)"

echo -n 'Stopping hadoop ...'
"$RES_DIR/scripts/stop-hadoop.sh" >/dev/null 2>&1
echo ok

rm -rf /tmp/hadoop*

echo -n 'Formatting namenode ... '
hdfs namenode -format >/dev/null 2>&1
echo ok

echo -n 'Starting hadoop ... '

start-all.sh >/dev/null 2>&1
echo ok
sleep 2

USERNAME="$(id -nu)"

echo -n 'Creating directories and uploading files at '"/user/$USERNAME/input ..."
hdfs dfs -mkdir -p /user/"$USERNAME"/input/
hdfs dfs -put "$RES_DIR"/*.txt /user/"$USERNAME"/input/
echo ok
