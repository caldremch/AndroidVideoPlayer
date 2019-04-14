//
// Created by Caldremch on 2019/4/14.
//

#include "FFmpegCore.h"
#include <jni.h>
#include "android_log.h"

extern "C"
JNIEXPORT void JNICALL
Java_com_caldremch_ffmpegcore_FFmpegCore_invokeTest2(JNIEnv *env, jclass type, jobjectArray cmds) {
    log_debug("调用成功");


}