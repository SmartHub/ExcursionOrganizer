#!/bin/sh

PWD=$(pwd)
cd $PWD/common/sql

if [ "$2" != "" ]
    then
        cat creation.sql | mysql -u $1 -p $2 excursion_organizer
        cat preload.sql  | mysql -u $1 -p $2 excursion_organizer
    else
        cat creation.sql | mysql -u $1 excursion_organizer
        cat preload.sql  | mysql -u $1 excursion_organizer
fi