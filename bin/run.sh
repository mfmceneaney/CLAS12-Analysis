#!/bin/bash

# Check if environment variable is set
if [ -z "$C12ANALYSIS_INSTALL_DIR" ]; then
    java $JAVA_OPTS -jar $C12ANALYSIS/app/build/libs/app-all-1.0.jar $@
    exit $?
else
    java $JAVA_OPTS -jar $C12ANALYSIS_INSTALL_DIR/app-all-1.0.jar $@
    exit $?
fi
