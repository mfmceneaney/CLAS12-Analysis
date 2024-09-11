#!/bin/bash
export DEPLOY_JAVA_HOME=$PWD
echo DEPLOY_JAVA_HOME=$DEPLOY_JAVA_HOME
export GCPATH=$GCPATH:$DEPLOY_JAVA_HOME/app/build/libs/app.jar:$DEPLOY_JAVA_HOME/app/classes/main/groovy/:$DEPLOY_JAVA_HOME/app/src/main/groovy/

# Set paths to pytorch and pytorch-geometric dependencies.  You may need to modify this if you have installed these somewhere else.
export LIBTORCH_HOME=$DEPLOY_JAVA_HOME/libtorch
export LIBTORCH_SPARSE_HOME=$DEPLOY_JAVA_HOME/pytorch_sparse
export LIBTORCH_SCATTER_HOME=$DEPLOY_JAVA_HOME/pytorch_scatter
export LIBTORCH_CLUSTER_HOME=$DEPLOY_JAVA_HOME/pytorch_cluster
export LIBTORCH_SPLINE_CONV_HOME=$DEPLOY_JAVA_HOME/pytorch_spline_conv
export LIBTORCH_PYG_LIB_HOME=$DEPLOY_JAVA_HOME/pyg-lib

export JAVA_OPTS=${JAVA_OPTS}:$LIBTORCH_HOME/lib:$LIBTORCH_SPARSE_HOME/build:$LIBTORCH_SCATTER_HOME/build:$LIBTORCH_CLUSTER_HOME/build:$LIBTORCH_SPLINE_CONV_HOME/build:$C12ANALYSIS/j2root/build:$LIBTORCH_PYG_LIB_HOME/build
#NOTE: THE ABOVE ASSUMES YOU'VE ALREADY SET $JAVA_OPTS WITH THE J2ROOT INSTALLATION -> JAVA_OPTS="-Djava.library.path=/path/to/j2root/build".

# Add command line interface to path
export PATH=$PATH:$DEPLOY_JAVA_HOME/bin
