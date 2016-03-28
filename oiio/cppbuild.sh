#!/bin/bash
# This file is meant to be included by the parent cppbuild.sh script
if [[ -z "$PLATFORM" ]]; then
    pushd ..
    bash cppbuild.sh "$@" oiio
    popd
    exit
fi

OIIO_VERSION=wex-java
OIIO_NAME=oiio-$OIIO_VERSION
echo "Building oiio..."
set -x

# Download and uncompress
download https://github.com/Zorroa/oiio/archive/${OIIO_VERSION}.zip $OIIO_NAME.tar.gz

#download https://github.com/OpenImageIO/oiio/archive/$OIIO_NAME.tar.gz $OIIO_NAME.tar.gz
mkdir -p $PLATFORM
cd $PLATFORM
echo "Decompressing archives"
tar --totals -xzf ../$OIIO_NAME.tar.gz
cd $OIIO_NAME

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
BUILD_FLAGS="USE_FREETYPE=0 USE_LIBRAW=0 USE_OPENCV=0 USE_OCIO=0 USE_CPP11=0 USE_PYTHON=0 OIIO_BUILD_TOOLS=0 OIIO_BUILD_TESTS=0 LINKSTATIC=1 OpenEXR_USE_STATIC_LIBS=1"
make VERBOSE=1 $BUILD_FLAGS cmakesetup
echo make -j2 $BUILD_FLAGS
make -j2 $BUILD_FLAGS

# Skip tests for now
# src/build-scripts/install_test_images.bash
# export OPENIMAGEIOHOME=$PWD/dist/$PLATFORM
# export PYTHONPATH=$OPENIMAGEIOHOME/python:$PYTHONPATH
# make $BUILD_FLAGS test

OIIO_PLATFORM=macosx

# Set the @rpath id for the libraries
install_name_tool -id "@rpath/libOpenImageIO_Util.dylib" dist/$OIIO_PLATFORM/lib/libOpenImageIO_Util.dylib 
install_name_tool -id "@rpath/libOpenImageIO_Util.1.7.dylib" dist/$OIIO_PLATFORM/lib/libOpenImageIO_Util.1.7.dylib 
install_name_tool -id "@rpath/libOpenImageIO_Util.1.7.2.dylib" dist/$OIIO_PLATFORM/lib/libOpenImageIO_Util.1.7.2.dylib 
install_name_tool -id "@rpath/libOpenImageIO.dylib" dist/$OIIO_PLATFORM/lib/libOpenImageIO.dylib 
install_name_tool -id "@rpath/libOpenImageIO.1.7.dylib" dist/$OIIO_PLATFORM/lib/libOpenImageIO.1.7.dylib 
install_name_tool -id "@rpath/libOpenImageIO.1.7.2.dylib" dist/$OIIO_PLATFORM/lib/libOpenImageIO.1.7.2.dylib 

# Copy dist to top level
cp -a dist/$OIIO_PLATFORM/include ..
cp -aL dist/$OIIO_PLATFORM/lib ..


cd ../..
