#!/bin/bash

# Build groovy library
cd $C12ANALYSIS
./gradlew build
./gradlew --stop
