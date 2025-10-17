#!/bin/bash

# Setup clasqaDB
cd $C12ANALYSIS/clasqaDB/src
jar -c -f clasqa.jar clasqa/*.groovy
# jar cf clasqa.jar clasqa/*.groovy # Depending on your jar version you might need this line instead
mv clasqa.jar ..
cd $C12ANALYSIS
