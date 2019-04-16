//
// Created by Caldremch on 2019/4/14.
//

#include "FFmpegCore.h"
#include <jni.h>
#include "android_log.h"
extern "C"{

    #include "ffmpeg/ffmpeg.h"


JNIEXPORT jint JNICALL
Java_com_caldremch_ffmpegcore_FFmpegCore_execCmd(JNIEnv *env, jclass type, jobjectArray cmds) {

    log_debug("调用开始");


    int argc = env->GetArrayLength(cmds);

    char *argv[argc];

    for (int i = 0; i < argc; ++i) {
        jstring js = (jstring)(env->GetObjectArrayElement(cmds, i));
        argv[i] = (char *)(env->GetStringUTFChars(js, 0));
        log_debug("命令:::JNI:%s", argv[i]);
    }
    int ret = jxRun(argc, argv);
    log_debug("命令:::返回:%d", ret);
    return ret;

}

}
