#!/bin/bash

export C12ANALYSIS=$PWD
echo "C12ANALYSIS=$PWD" | grep C12ANALYSIS --color=auto

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
# jar cf clasqa.jar clasqa/*.groovy # Depending on your jar version you might need this line instead
mv clasqa.jar ..
cd $C12ANALYSIS

# Build groovy library
./gradlew build
