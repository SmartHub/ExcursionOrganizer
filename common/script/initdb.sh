#!/bin/sh

PWD=$(pwd)

cd ../sql
cat creation.sql | mysql -u exorg excursion_organizer
cat preload.sql  | mysql -u exorg excursion_organizer

cd $PWD
if [[ ! -a ../../frontend/index ]]; then mkdir ../../frontend/index; fi