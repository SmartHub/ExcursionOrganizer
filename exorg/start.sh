#!/bin/sh

EO_ROOT=$(pwd)
export EO_ROOT

#chmod +x $EO_ROOT/core/script/initdb.sh
#chmod +x $EO_ROOT/core/script/daemonize
#chmod +x $EO_ROOT/processing/script/index.py

username=$(awk '/jdbc.username/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')
password=$(awk '/jdbc.password/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')

#echo "create or recreate DB"
#$EO_ROOT/core/script/initdb.sh $username $password

EO_LIB=$EO_ROOT/lib
EO_CP=$(echo $(find $EO_LIB -follow -name *jar) | sed 's/ /:/g')

#echo "run miner"
#cd $EO_ROOT/miner/config
#java -cp $EO_CP ru.exorg.core.util.Starter config.xml $EO_ROOT

#echo "run processing"
#cd $EO_ROOT/processing/config
#java -cp $EO_CP ru.exorg.core.util.Starter config.xml $EO_ROOT

#echo "Indexing database..."
#rm $EO_ROOT/backend/index/*
#cd $EO_ROOT/indexer/config
#java -cp $EO_CP ru.exorg.core.util.Starter config.xml $EO_ROOT

echo "Running backend HTTP server"
cd $EO_ROOT/backend/config
java -cp $EO_CP ru.exorg.core.util.Starter config.xml $EO_ROOT

echo "FINISH"
