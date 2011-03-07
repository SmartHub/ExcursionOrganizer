#!/bin/sh

java -cp $(echo $(find common/lib -follow -name *jar) | sed 's/ /:/g') \
     net.sf.xfresh.util.Starter miner/config/config.xml
