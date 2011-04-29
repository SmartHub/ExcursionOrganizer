#!/bin/sh

if [ "$2" != "" ]
    then
        cat creation.sql | mysql -u $1 -p $2 excursion_organizer
        cat preload.sql  | mysql -u $1 -p $2 excursion_organizer
    else
        cat creation.sql | mysql -u $1 excursion_organizer
        cat preload.sql  | mysql -u $1 excursion_organizer
fi

cd $EO_ROOT
#if [[ -n file ]]; then echo "Not found"; fi;
if [[ ! -a $EO_ROOT/backend/index ]]; then mkdir $EO_ROOT/backend/index; fi
