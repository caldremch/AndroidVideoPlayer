//
// Created by Caldremch on 2019/4/14.
//

#ifndef ANDROIDVIDEOPLAYER_ANDROID_LOG_H
#define ANDROIDVIDEOPLAYER_ANDROID_LOG_H

#include <android/log.h>

//#ifndef CORERELEASE
#define log_info(fmt, ...) __android_log_print(ANDROID_LOG_INFO, "FFmpegCoreTag",fmt, ##__VA_ARGS__)
#define log_debug(fmt, ...) __android_log_print(ANDROID_LOG_VERBOSE, "FFmpegCoreTag",fmt,##__VA_ARGS__)
#define log_error(fmt, ...) __android_log_print(ANDROID_LOG_INFO, "FFmpegCoreTag",fmt,##__VA_ARGS__)
//

//#else
//
//#define log_info(fmt, ...) NULL
//#define log_debug(fmt, ...) NULL
//#define log_error(fmt, ...) NULL
//
//#endif

#endif //ANDROIDVIDEOPLAYER_ANDROID_LOG_H
