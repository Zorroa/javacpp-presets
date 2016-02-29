#!/bin/bash
# This file is meant to be included by the parent cppbuild.sh script
if [[ -z "$PLATFORM" ]]; then
    pushd ..
    bash cppbuild.sh "$@" oiio
    popd
    exit
fi

OIIO_VERSION=1.6.10

# Download and uncompress
download https://github.com/OpenImageIO/oiio/archive/Release-$OIIO_VERSION.tar.gz oiio-$OIIO_VERSION.tar.gz
mkdir -p $PLATFORM
cd $PLATFORM
echo "Decompressing archives"
tar --totals -xzf ../oiio-$OIIO_VERSION.tar.gz
cd oiio-Release-$OIIO_VERSION

# Download additional dependencies and configure environment
case $PLATFORM in
    linux-x86)
        export CXX="ccache $CXX" src/build-scripts/build_openexr.bash
        export OPENEXR_HOME=$PWD/openexr-install 
        export EXTRA_LINK_ARGS="--copt=-m32 --linkopt=-m32"
        export DYLD_LIBRARY_PATH=$OPENIMAGEIOHOME/lib:$DYLD_LIBRARY_PATH 
        export LD_LIBRARY_PATH=$OPENIMAGEIOHOME/lib:$LD_LIBRARY_PATH
        ;;
    linux-x86_64)
        export CXX="ccache $CXX" src/build-scripts/build_openexr.bash
        export OPENEXR_HOME=$PWD/openexr-install 
        export EXTRA_LINK_ARGS="--copt=-m64 --linkopt=-m64"
        export DYLD_LIBRARY_PATH=$OPENIMAGEIOHOME/lib:$DYLD_LIBRARY_PATH 
        export LD_LIBRARY_PATH=$OPENIMAGEIOHOME/lib:$LD_LIBRARY_PATH
        ;;
    macosx-*)
        export EXTRA_LINK_ARGS="--linkopt=-install_name --linkopt=@rpath/liboiio.so"
        src/build-scripts/install_homebrew_deps.bash
        brew install freetype
        ;;
    *)
        echo "Error: Platform \"$PLATFORM\" is not supported"
        return 0
        ;;
esac

# Build
BUILD_FLAGS="USE_CPP11=0 USE_PYTHON=0 OIIO_BUILD_TOOLS=0 OIIO_BUILD_TESTS=0"
make VERBOSE=1 $BUILD_FLAGS cmakesetup
echo make -j2 $BUILD_FLAGS
make -j2 $BUILD_FLAGS

# Skip tests for now
# src/build-scripts/install_test_images.bash
# export OPENIMAGEIOHOME=$PWD/dist/$PLATFORM
# export PYTHONPATH=$OPENIMAGEIOHOME/python:$PYTHONPATH
# make $BUILD_FLAGS test

# Patch a couple of oiio items that we'll fix in a future version.
# Notably the use of an argument with the reserved word 'native'
# and a C++-style comment that embeds a C-style comment.
patch dist/macosx/include/OpenImageIO/imageio.h < ../../../oiio-imageio-$OIIO_VERSION.patch
patch dist/macosx/include/OpenImageIO/imagecache.h < ../../../oiio-imagecache-$OIIO_VERSION.patch
patch dist/macosx/include/OpenImageIO/imagebufalgo_util.h < ../../../oiio-imagebufalgo_util-$OIIO_VERSION.patch

# Copy dist to top level
OIIO_PLATFORM=macosx
cp -a dist/$OIIO_PLATFORM/include ..
cp -aL dist/$OIIO_PLATFORM/lib ..


cd ../..
