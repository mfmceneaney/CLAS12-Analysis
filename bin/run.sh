#!/bin/bash

groovy -cp $CLASSPATH\:$C12ANALYSIS/lib/build/libs/lib.jar $C12ANALYSIS/lib/src/main/groovy/org/jlab/analysis/Main.groovy $@
