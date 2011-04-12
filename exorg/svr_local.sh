#!/bin/sh

case $1 in 
    start) 
        echo "Starting..."
        frontend/http_server/script/run.py -D $2 $3
        ;;

    stop) 
        echo "Stopping..."
        killall java
		killall searchd
        ;;
esac
