#!/bin/sh

mkdir svr-distr
mkdir svr-distr/core
mkdir svr-distr/lib

cp -L -R lib/eo.jar svr-distr/lib
cp -R core/script svr-distr/core

mkdir svr-distr/backend

cp -R backend/content svr-distr/backend
cp -R backend/config  svr-distr/backend

rm svr.tar.bz2
cd svr-distr
for f in $(find ./ | grep -e "\.log$"); do rm $f; done
tar -cf svr.tar *
bzip2 svr.tar
mv svr.tar.bz2 ../
cd ..
rm -rf svr-distr
