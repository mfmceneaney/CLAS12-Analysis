#!/bin/bash

#groovy -cp $CLASSPATH\:$GCPATH $C12ANALYSIS/app/src/main/groovy/org/jlab/analysis/Main.groovy $@
$DEPLOY_JAVA_HOME/gradlew run -q -p $DEPLOY_JAVA_HOME --args="`echo " $@"`"
