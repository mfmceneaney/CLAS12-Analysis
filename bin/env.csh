#!/bin/bash
setenv C12ANALYSIS $PWD
setenv GCPATH $C12ANALYSIS/lib/build/libs/

# J2ROOT Setup
setenv J2ROOT $C12ANALYSIS/j2root
cd $J2ROOT; source setup.csh; cd ..;
#NOTE: j2root/setup.csh resets $JYPATH so make sure it is called before the clasqa/env.csh script!

# CLASQA Setup
cd clasqaDB; source env.csh; cd ..;

# Add command line interface to path
setenv PATH PATH:$C12ANALYSIS/bin
