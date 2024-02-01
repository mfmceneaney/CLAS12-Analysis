#!/bin/csh

setenv C12ANALYSIS $PWD
echo "C12ANALYSIS=$PWD" | grep C12ANALYSIS --color=auto

# Setup j2root
cd j2root
javac -h build/native src/main/java/org/jlab/jroot/JRootJNI.java
sed -i.bak 's;^env.JavaH;#env.JavaH;g' sconscript
scons
mvn package
sed -i.bak 's;^set sourced;set sourced=\(\"source\" \"j2root/setup.csh\"\)#set sourced ; g' setup.csh
cd $C12ANALYSIS

# Setup clasqaDB
cd clasqaDB/src
jar -c -f clasqa.jar clasqa/*.groovy
# jar cf clasqa.jar clasqa/*.groovy # Depending on your jar version you might need this line instead
mv clasqa.jar ..
cd $C12ANALYSIS

# Build groovy library
./gradlew build
