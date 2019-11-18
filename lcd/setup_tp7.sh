rm -rf /tmp/hadoop*
hdfs namenode -format
start-dfs.sh
hdfs dfs -mkdir -p /user/jguyot2/input/
hdfs dfs -put tp7_textes/*  /user/jguyot2/input/
mkdir ~/.sparklogs
start-master.sh
firefox http://localhost:9090/
