#!/bin/bash

mvn clean install

cd ./client/target
tar -xzf tpe2-g10-client-1.0-SNAPSHOT-bin.tar.gz
chmod -R +x ./tpe2-g10-client-1.0-SNAPSHOT

cd ../../server/target
tar -xzf tpe2-g10-server-1.0-SNAPSHOT-bin.tar.gz
chmod -R +x ./tpe2-g10-server-1.0-SNAPSHOT
cd ../..
