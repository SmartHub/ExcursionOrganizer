#!/bin/sh

cat common/script/creation.sql | mysql -u exorg excursion_organizer
cat common/script/preload.sql | mysql -u exorg excursion_organizer
