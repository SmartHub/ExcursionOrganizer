#!/bin/sh

PWD=$(pwd)

cd ../sql
cat creation.sql | mysql -u root excursion_organizer
cat preload.sql  | mysql -u root excursion_organizer

cd $PWD
if [[ ! -a ../../frontend/index ]]; then mkdir ../../frontend/index; fi
