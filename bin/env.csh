#!/bin/bash
setenv C12ANALYSIS $PWD
setenv GCPATH $C12ANALYSIS/lib/build/libs/lib.jar:$C12ANALYSIS/lib/classes/main/groovy/:$C12ANALYSIS/lib/src/main/groovy/:$C12ANALYSIS/j2root/target/classes/:$C12ANALYSIS/clasqaDB/clasqa.jar

# J2ROOT Setup
setenv J2ROOT $C12ANALYSIS/j2root
cd $J2ROOT; source setup.csh; cd ..;
setenv JAVA_OPTS "$JAVA_OPTS -Djava.library.path=$J2ROOT/build" 
#NOTE: j2root/setup.csh resets $JYPATH so make sure it is called before the clasqa/env.csh script!

# CLASQA Setup
cd clasqaDB; source env.csh; cd ..;

# Add command line interface to path
setenv PATH $PATH\:$C12ANALYSIS/bin
