#!/bin/sh

cd $EO_ROOT/frontend/http_server/src/config

searchd -c $EO_ROOT/common/script/exorg.sphinx

java -cp $EO_CP \
     eo.common.Starter config.xml
