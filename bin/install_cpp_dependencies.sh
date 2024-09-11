#!/bin/bash

export PROJECT_DIR=/work/clas12/users/mfmce/test_pyg_CLAS12-Analysis/CLAS12-Analysis
export VENV_PACKAGES=/work/clas12/users/mfmce/venv_ifarm/lib/python3.9/site-packages

cd $PROJECT_DIR

# Install PyTorch Sparse
git clone --recurse-submodules https://github.com/rusty1s/pytorch_sparse.git
pushd pytorch_sparse
mkdir -p build
cd build
cmake -DCXX_STANDARD="c++17" -DCMAKE_PREFIX_PATH="$PROJECT_DIR/libtorch;$VENV_PACKAGES/torch" ../
make
mkdir install
cmake --install . --prefix $PWD/install/
popd

# Install PyTorch Scatter
git clone https://github.com/rusty1s/pytorch_scatter.git
pushd pytorch_scatter
mkdir -p build
cd build
cmake -DCMAKE_PREFIX_PATH=$PROJECT_DIR/libtorch ..
make
mkdir install
cmake --install . --prefix $PWD/install/
popd

# Install PyTorch Cluster
git clone https://github.com/rusty1s/pytorch_cluster.git
pushd pytorch_cluster
mkdir -p build
cd build
cmake -DCMAKE_PREFIX_PATH=$PROJECT_DIR/libtorch ..
make
mkdir install
cmake --install . --prefix $PWD/install/
popd

# Install PyTorch Spline Conv
git clone https://github.com/rusty1s/pytorch_spline_conv.git
pushd pytorch_spline_conv
mkdir -p build
cd build
cmake -DCMAKE_PREFIX_PATH=$PROJECT_DIR/libtorch ..
make
mkdir install
cmake --install . --prefix $PWD/install/
popd

echo DONE
