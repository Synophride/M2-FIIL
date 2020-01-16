rm -rf /tmp/hadoop*
hdfs namenode -format
start-dfs.sh
hdfs dfs -mkdir -p /user/jguyot2/input/
hdfs dfs -put tp6_textes/*  /user/jguyot2/input/

