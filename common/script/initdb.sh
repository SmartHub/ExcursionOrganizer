#!/bin/sh

cat ../sql/creation.sql | mysql -u exorg excursion_organizer
cat ../sql/preload.sql  | mysql -u exorg excursion_organizer
