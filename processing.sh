#!/bin/sh

LC_NUMERIC=en_US.UTF-8

java -cp $(echo $(find common/lib -follow -name *jar) | sed 's/ /:/g') \
     net.sf.xfresh.util.Starter processing/config/config.xml
