#!/bin/sh

EO_ROOT=$(pwd)

username=$(awk '/jdbc.username/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')
password=$(awk '/jdbc.password/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')

#echo "create or recreate DB"
#$EO_ROOT/common/script/initdb.sh $username $password

EO_LIB=$EO_ROOT/lib

EO_CP=$(echo $(find $EO_LIB -follow -name *jar) | sed 's/ /:/g')

#echo "run miner"
#cd $EO_ROOT/miner/config
#java -cp $EO_CP \
#    ru.exorg.core.util.Starter config.xml $EO_ROOT

#echo "run processing"
#cd $EO_ROOT/processing/config
#java -cp $EO_CP \
#     ru.exorg.core.util.Starter config.xml $EO_ROOT

#echo "index database"
#python $EO_ROOT/processing/config/index.py $EO_ROOT $username $password

echo "start search daemon"
searchd -c $EO_ROOT/backend/exorg.sphinx

echo "Running frontend HTTP server"
cd $EO_ROOT/frontend/config
$EO_ROOT/core/script/daemonize java -cp $EO_CP ru.exorg.core.util.Starter config.xml $EO_ROOT

echo "Running backend HTTP server"
cd $EO_ROOT/backend/config
#$EO_ROOT/core/script/daemonize 
java -cp $EO_CP ru.exorg.core.util.Starter config.xml $EO_ROOT

echo "FINISH"
