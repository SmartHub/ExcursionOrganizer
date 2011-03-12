#!/bin/sh

cat common/script/creation.sql | mysql -u exorg excursion_organizer
cat common/script/poi_types.sql | mysql -u exorg excursion_organizer
