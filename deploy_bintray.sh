#!/bin/bash

modules=("meaoo-client" "meaoo-client-android")
for i in "${modules[@]}"
do
echo ">> Deploying $i ..."
./gradlew :$i:clean :$i:build :$i:bintrayUpload -PbintrayUser=$BINTRAY_USER -PbintrayKey=$BINTRAY_KEY -PdryRun=false
echo ">> Done deploying for $i"
done
