#! /bin/bash

DATE=`date '+%Y-%m-%d %H:%M:%S'`


cd backend/
git pull origin master
mvn clean install assembly:single
java -jar target/atp-0.1-jar-with-dependencies.jar  -debug -cmd rankings -dir "$PWD/../data/"
cd ../
git add  data/*
git commit -m "Cron push rankings $DATE"
git push origin master


