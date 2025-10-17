#!/bin/bash

# Setup j2root
cd $C12ANALYSIS/j2root
javac -h build/native src/main/java/org/jlab/jroot/JRootJNI.java
sed -i.bak 's;^env.JavaH;#env.JavaH;g' sconscript
scons
mvn package
sed -i.bak 's;^set sourced;set sourced=\(\"source\" \"j2root/setup.csh\"\)#set sourced ; g' setup.csh
cd $C12ANALYSIS
