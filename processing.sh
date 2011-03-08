#!/bin/sh

java -cp $(echo $(find common/lib -follow -name *jar) | sed 's/ /:/g') \
     net.sf.xfresh.util.Starter processing/config/config.xml
