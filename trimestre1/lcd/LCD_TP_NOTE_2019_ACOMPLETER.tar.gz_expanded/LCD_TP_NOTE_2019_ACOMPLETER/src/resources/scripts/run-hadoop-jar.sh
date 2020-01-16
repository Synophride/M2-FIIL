#!/bin/sh

PATH="/public/kn/hadoop-3.1.2/sbin:/public/kn/hadoop-3.1.2/bin:$PATH"
HADOOP_HOME="/public/kn/hadoop-3.1.2"
RES_DIR="$(dirname "$0")"
RES_DIR="$(realpath "$RES_DIR"/..)"
BIN_DIR="$(realpath "$RES_DIR"/..)"

#"$RES_DIR"/scripts/mkjar.sh

echo  Création du .jar
jar cf /tmp/exo2.jar -C "$BIN_DIR" .


HDFS_DIR="/user/$(id -nu)"

hdfs dfs -rm -r "$HDFS_DIR"/output >/dev/null

echo Lancement du job

hadoop jar /tmp/exo2.jar lcd.tpnote.exo3."$1" "$HDFS_DIR"/output "$HDFS_DIR"/input/capitals_distances.txt >/tmp/hadoop_exo2.log 2>&1

if hdfs dfs -test -f "$HDFS_DIR"/output/_SUCCESS
then
	echo Job exécuté avec succès
	echo Résultat :
	hdfs dfs -cat "$HDFS_DIR"/output/\*
else
	cat /tmp/hadoop_exo2.log >&2
fi