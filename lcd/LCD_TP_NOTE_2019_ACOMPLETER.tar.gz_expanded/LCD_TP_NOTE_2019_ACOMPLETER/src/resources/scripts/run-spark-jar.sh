#!/bin/sh


PATH="/public/kn/spark-2.4.4-bin-without-hadoop/bin:/public/kn/spark-2.4.4-bin-without-hadoop/sbin:/public/kn/hadoop-3.1.2/sbin:/public/kn/hadoop-3.1.2/bin:$PATH"
HADOOP_HOME="/public/kn/hadoop-3.1.2"
SPARK_HOME="/public/kn/spark-2.4.4-bin-without-hadoop"
RES_DIR="$(dirname "$0")"
RES_DIR="$(realpath "$RES_DIR"/..)"
BIN_DIR="$(realpath "$RES_DIR"/..)"

"$RES_DIR"/scripts/mkjar.sh

echo  Création du .jar
jar cf /tmp/exo3.jar -C "$BIN_DIR" .

echo -n "Exécution du job spark ... "
if spark-submit --master "spark://$(hostname):7077" --class lcd.tpnote.exo4."$1" /tmp/exo3.jar >/tmp/log.spark 2>&1
then
	echo "ok"
	echo "Résultat :"
	cat /tmp/log.spark | grep -v ' INFO ' | grep -v ' WARN '
else
	echo "erreur"
	cat /tmp/log.spark
fi