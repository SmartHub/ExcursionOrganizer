#!/bin/sh

PWD=$(pwd)

echo "\nqualitative tests:\n"
cat qualitative_tests.sql   | mysql -u exorg excursion_organizer
echo "\nquantitative tests:\n"
cat quantitative_tests.sql  | mysql -u exorg excursion_organizer
