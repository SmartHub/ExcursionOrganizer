#!/bin/sh

cd $EO_ROOT/frontend/http_server/src/config

java -cp $EO_CP \
     eo.common.Starter config.xml
