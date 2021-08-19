#!/bin/bash
export C12ANALYSIS=$PWD
export GCPATH=$C12ANALYSIS/lib/build/libs/

# J2ROOT Setup
export J2ROOT=$C12ANALYSIS/j2root
cd $J2ROOT; source setup.sh; cd ..;
export JAVA_OPTS="$JAVA_OPTS -Djava.library.path=$J2ROOT/build" 
#NOTE: j2root/setup.sh resets $JYPATH so make sure it is called before the clasqa/env.sh script!

# CLASQA Setup
cd clasqaDB; source env.sh; cd ..;

# Add command line interface to path
export PATH=$PATH:$C12ANALYSIS/bin
