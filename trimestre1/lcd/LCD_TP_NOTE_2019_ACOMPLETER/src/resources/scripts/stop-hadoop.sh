#!/bin/sh

PATH="/public/kn/hadoop-3.1.2/sbin:/public/kn/hadoop-3.1.2/bin:$PATH"
HADOOP_HOME="/public/kn/hadoop-3.1.2"

RES_DIR="$(dirname "$0")"
RES_DIR="$(realpath "$RES_DIR"/..)"


stop-dfs.sh >/dev/null 2>&1 || true
pkill -f 'java .*org[.]apache[.]hadoop' || true

rm -rf /tmp/hadoop*
