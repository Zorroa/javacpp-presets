package org.bytedeco.javacpp.presets;

import org.bytedeco.javacpp.FunctionPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.Cast;
import org.bytedeco.javacpp.annotation.Platform;
import org.bytedeco.javacpp.annotation.Properties;
import org.bytedeco.javacpp.tools.Info;
import org.bytedeco.javacpp.tools.InfoMap;
import org.bytedeco.javacpp.tools.InfoMapper;

@Properties(target="org.bytedeco.javacpp.oiio", value={
    @Platform(include={/*"OpenImageIO/SHA1.h",*/ "OpenImageIO/filter.h", /*"OpenImageIO/missing_math.h",*/
            /*"OpenImageIO/pugixml.hpp",*/ /*"OpenImageIO/timer.h",*/ /*"OpenImageIO/argparse.h",*/
            /*"OpenImageIO/fmath.h",*/ /*"OpenImageIO/oiioversion.h",*/ /*"OpenImageIO/refcnt.h",*/
            /*"OpenImageIO/tinyformat.h",*/ "OpenImageIO/array_view.h", /*"OpenImageIO/hash.h",*/
            "OpenImageIO/optparser.h", /*"OpenImageIO/simd.h",*/ "OpenImageIO/typedesc.h",
            /*"OpenImageIO/color.h",*/ "OpenImageIO/image_view.h", /*"OpenImageIO/osdep.h",*/
            "OpenImageIO/strided_ptr.h", /*"OpenImageIO/unittest.h",*/ "OpenImageIO/coordinate.h",
            "OpenImageIO/imagebuf.h", "OpenImageIO/paramlist.h", "OpenImageIO/string_view.h",
            /*"OpenImageIO/unordered_map_concurrent.h",*/ /*"OpenImageIO/dassert.h",*/
            /*"OpenImageIO/imagebufalgo.h",*/ /*"OpenImageIO/platform.h",*/ /*"OpenImageIO/strutil.h",*/
            "OpenImageIO/ustring.h", "OpenImageIO/errorhandler.h", "OpenImageIO/imagebufalgo_util.h",
            "OpenImageIO/plugin.h", /*"OpenImageIO/sysutil.h",*/ /*"OpenImageIO/varyingref.h",*/
            /*"OpenImageIO/export.h",*/ "OpenImageIO/imagecache.h", /*"OpenImageIO/pugiconfig.hpp",*/
            /*"OpenImageIO/texture.h",*/ "OpenImageIO/version.h", /*"OpenImageIO/filesystem.h",*/
            "OpenImageIO/imageio.h", /*"OpenImageIO/pugixml.cpp",*/ /*"OpenImageIO/thread.h"*/},
            link={"OpenImageIO.1.7", "OpenImageIO_Util.1.7"}) })
public class oiio implements InfoMapper {
    public void map(InfoMap infoMap) {
        infoMap
                .put(new Info("OIIO_CPLUSPLUS_VERSION >= 11").define(false))
                .put(new Info("OIIO_API").cppText("#define OIIO_API"))
                .put(new Info("OIIO_NAMESPACE_BEGIN").cppText("#define OIIO_NAMESPACE_BEGIN namespace OpenImageIO { "))
                .put(new Info("OIIO_NAMESPACE_END").cppText("#define OIIO_NAMESPACE_END }; "))
                .put(new Info("OIIO_FORCEINLINE").cppText("#define OIIO_FORCEINLINE"))
                .put(new Info("TINYFORMAT_WRAP_FORMAT").cppText("#define TINYFORMAT_WRAP_FORMAT(VarArg)"))
                .put(new Info("stride_t").valueTypes("long"))
                .put(new Info("imagesize_t").valueTypes("long"))
                .put(new Info("std::reverse_iterator<char>").skip())
                .put(new Info("const_reverse_iterator").skip())
                .put(new Info("const_iterator").skip())
                .put(new Info("std::string::const_iterator").skip())
                .put(new Info("std::string::const_reverse_iterator").skip())
                .put(new Info("OpenImageIO::ustringHashIsLess").skip())
                .put(new Info("OpenImageIO::ErrorHandler::verbosity").skip())
                .put(new Info("OpenImageIO::string_view::const_iterator").skip())
                .put(new Info("OpenImageIO::string_view::const_reverse_iterator").skip())
                .put(new Info("OpenImageIO::ParamValue::interp").skip())
                .put(new Info("OpenImageIO::ParamValueList::reference").skip())
                .put(new Info("OpenImageIO::ParamValueList::const_reference").skip())
                .put(new Info("OpenImageIO::ImageInput::threads").skip())
                .put(new Info("OpenImageIO::ImageOutput::threads").skip())
                .put(new Info("OpenImageIO::ImageBuf::threads").skip())
                .put(new Info("OpenImageIO::ImageBuf::get_pixel_channels").skip())
                .put(new Info("OpenImageIO::ImageBuf::get_pixels").skip())
                .put(new Info("OpenImageIO::ImageBuf::write").skip())               // FIXME: Too important to skip!
                .put(new Info("std::vector<std::string>").pointerTypes("StringVector").define())
                .put(new Info("Rep::iterator", "Rep::const_iterator").cast().pointerTypes("Pointer"))
                .put(new Info("(int)TypeDesc::UINT8").cppTypes("OpenImageIO::TypeDesc::UINT8"))
                .put(new Info("Creator", "ImageInput::Creator").valueTypes("CreatorFunctionPointer"))
                .put(new Info("ProgressCallback", "OpenImageIO::ProgressCallback").valueTypes("ProgressCallbackFunctionPointer"))
//                .put(new Info("__cplusplus").define())
//                .put(new Info("OIIO_SIMD").define(false))
//                .put(new Info("OIIO_STATIC_BUILD").define(false))
//                .put(new Info("(10000*__GNUC__ + 100*__GNUC_MINOR__ + __GNUC_PATCHLEVEL__) > 40102").define(false))
//                .put(new Info("defined(_MSC_VER) || defined(__CYGWIN__)").define(false))
//                .put(new Info("OIIO_SIMD_SSE >= 3").define(false))
//                .put(new Info("TCHAR").define())
//                .put(new Info("get_local_time").skip())
//                .put(new Info("UINT_8").valueTypes("byte"))
//                .put(new Info("UINT_32").valueTypes("int"))
//                .put(new Info("ImageInput").cppTypes("OpenImageIO:ImageInput"))
//                .put(new Info("ImageOutput").cppTypes("OpenImageIO:ImageOutput"))
//                .put(new Info("OpenImageIO::ImageInput::read_image").skip())
//                .put(new Info(/*"string_view",*/ "ustring").pointerTypes("String"))
//                .put(new Info("int").valueTypes("int").pointerTypes("IntPointer", "IntBuffer", "int..."))
//                .put(new Info("long long").cast().valueTypes("long").pointerTypes("LongPointer", "LongBuffer", "long..."))
//                .put(new Info("float").valueTypes("float").pointerTypes("FloatPointer", "FloatBuffer", "float..."))
//                .put(new Info("double").valueTypes("double").pointerTypes("DoublePointer", "DoubleBuffer", "double..."))
//                .put(new Info("bool").cast().valueTypes("boolean").pointerTypes("BoolPointer", "boolean..."))
//                .put(new Info("boost::remove_const", "boost::is_array").skip())
        ;
    }

    public static class CreatorFunctionPointer extends FunctionPointer {
        static { Loader.load(); }
        public CreatorFunctionPointer(Pointer p) { super(p); }
        public CreatorFunctionPointer() { allocate(); }
        private native void allocate();
        public native @Cast("OpenImageIO::v1_7::ImageInput *") Pointer call();
    }

    public static class ProgressCallbackFunctionPointer extends FunctionPointer {
        static { Loader.load(); }
        public ProgressCallbackFunctionPointer(Pointer p) { super(p); }
        public ProgressCallbackFunctionPointer() { allocate(); }
        private native void allocate();
        public native @Cast("bool") boolean call(Pointer data, float progress);
    }
}
