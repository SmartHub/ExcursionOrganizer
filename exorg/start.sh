#!/bin/sh

EO_ROOT=$(pwd)

username=$(awk '/jdbc.username/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')
password=$(awk '/jdbc.password/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')

echo "create or recreate DB"
#$EO_ROOT/common/script/initdb.sh $username $password

EO_LIB=$EO_ROOT/lib

EO_CP=$(echo $(find $EO_LIB -follow -name *jar) | sed 's/ /:/g')

echo "run miner"
cd $EO_ROOT/miner/config
java -cp $EO_CP \
    eo.common.util.Starter config.xml $EO_ROOT

echo "run processing"
#cd $EO_ROOT/processing/config
#java -cp $EO_CP \
#     eo.common.util.Starter config.xml $EO_ROOT

echo "index database"
#$EO_ROOT/common/script/index.py $EO_ROOT $username $password

echo "run server"
#cd $EO_ROOT
#./svr_local.sh start $EO_ROOT $EO_CP
