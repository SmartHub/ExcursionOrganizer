#!/bin/sh

cd $EO_ROOT/processing/src/config

java -cp $EO_CP \
     eo.common.Starter config.xml
