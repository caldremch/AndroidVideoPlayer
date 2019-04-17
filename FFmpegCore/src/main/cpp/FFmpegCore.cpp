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
    av_register_all();

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
extern "C"
JNIEXPORT jstring JNICALL
Java_com_caldremch_ffmpegcore_FFmpegCore_invokeTest(JNIEnv *env, jclass type) {


    char info[40000] = {0};
    av_register_all();
    AVInputFormat *if_temp = av_iformat_next(NULL);
    AVOutputFormat *of_temp = av_oformat_next(NULL);
    while (if_temp != NULL) {
        sprintf(info, "%sInput: %s\n", info, if_temp->name);
        if_temp = if_temp->next;
    }
    while (of_temp != NULL) {
        sprintf(info, "%sOutput: %s\n", info, of_temp->name);
        of_temp = of_temp->next;
    }

    //ffmpeg 命令执行
    return env->NewStringUTF(info);

}