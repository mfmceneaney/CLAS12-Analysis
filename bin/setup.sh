#!/bin/bash

export C12ANALYSIS=$PWD

# Setup j2root
git clone https://github.com/drewkenjo/j2root.git
cd j2root
javac -h build/native -d src/main/java/org/jlab/jroot/JRootJNI src/main/java/org/jlab/jroot/JRootJNI.java
sed -i.bak 's;env.JavaH;#env.JavaH;g' sconscript
scons
mvn package
cd $C12ANALYSIS

# Setup clasqaDB
git clone https://github.com/JeffersonLab/clasqaDB.git
cd clasqaDB/src
jar -c -f clasqa.jar clasqa/*.groovy
mv clasqa.jar ..
cd $C12ANALYSIS

# Build groovy library
./gradlew build
