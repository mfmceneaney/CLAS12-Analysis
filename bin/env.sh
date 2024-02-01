#!/bin/bash
export C12ANALYSIS=$PWD
export GCPATH=$C12ANALYSIS/app/build/libs/app.jar:$C12ANALYSIS/app/classes/main/groovy/:$C12ANALYSIS/app/src/main/groovy/:$C12ANALYSIS/j2root/target/classes/:$C12ANALYSIS/clasqaDB/clasqa.jar

# J2ROOT Setup
export J2ROOT=$C12ANALYSIS/j2root
source $J2ROOT/setup.sh
#NOTE: j2root/setup.sh resets $JYPATH so make sure it is called before the clasqa/env.sh script!

# CLASQA Setup
cd clasqaDB; source environ.sh; cd ..;

# Add command line interface to path
export PATH=$PATH:$C12ANALYSIS/bin

# Show new environment variables
env | grep C12ANALYSIS --color=auto
env | grep GCPATH --color=auto
env | grep J2ROOT --color=auto
