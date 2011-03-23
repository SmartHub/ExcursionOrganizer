#!/bin/sh

export EO_ROOT=$(pwd)
export EO_LIB=$EO_ROOT/common/lib

export EO_CP=$(echo $(find $EO_LIB -follow -name *jar) | sed 's/ /:/g')