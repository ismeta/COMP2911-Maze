#!/bin/sh

# directory
echo "recreating ass3"
rm -rf ass3
mkdir -p ass3

# copy shit
echo "copying files over"
cp -vr src ass3
cp -vr doc ass3
cp -vr images ass3
cp -vr music ass3
cp -v uml/uml.pdf ass3
cp -v compile ass3
cp -v run ass3

# make a zip
echo "generating zip ass3.zip"
cd ./ass3
zip -r ../ass3.zip *
