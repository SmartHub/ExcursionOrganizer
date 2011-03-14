#!/bin/sh

java -cp $(echo $(find common/lib -follow -name *jar) | sed 's/ /:/g') \
     eo.common.Starter frontend/http_server/config/config.xml
