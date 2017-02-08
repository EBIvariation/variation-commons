#!/usr/bin/env sh

git clone -b hotfix/0.4 https://github.com/ebivariation/biodata.git
git clone -b hotfix/0.5 https://github.com/ebivariation/opencga.git

cd biodata && mvn install
cd ..
cd opencga && mvn install -DskipTests

