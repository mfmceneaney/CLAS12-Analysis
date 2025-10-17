#!/bin/bash

export C12ANALYSIS=$PWD
export C12ANALYSIS_INSTALL_DIR=$C12ANALYSIS/app/build/libs
env | grep C12ANALYSIS --color=auto

# Build j2root
$C12ANALYSIS/bin/build_j2root.sh

# Build clasqaDB
$C12ANALYSIS/bin/build_clasqaDB.sh

# Build groovy library
$C12ANALYSIS/bin/build_gradle.sh
