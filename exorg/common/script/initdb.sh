#!/bin/bash

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

cd $PWD
#if [[ -n file ]]; then echo "Not found"; fi;
if [[ ! -a ../../frontend/index ]]; then mkdir ../../frontend/index; fi
