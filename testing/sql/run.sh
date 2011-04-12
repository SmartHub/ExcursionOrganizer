#!/bin/sh

EO_ROOT=$(pwd)/../..

username=$(awk '/jdbc.username/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')
password=$(awk '/jdbc.password/' $EO_ROOT/custom.properties | awk  -F= '{print $2}')

echo "\nqualitative tests:\n"
if [ "$password" != "" ]
	then
		cat qualitative_tests.sql   | mysql -u $username -p $password excursion_organizer
	else
		cat qualitative_tests.sql   | mysql -u $username excursion_organizer
fi
echo "\nquantitative tests:\n"
if [ "$password" != "" ]
	then
		cat quantitative_tests.sql   | mysql -u $username -p $password excursion_organizer
	else
		cat quantitative_tests.sql   | mysql -u $username excursion_organizer
fi
