//
// Created by Caldremch on 2019/4/14.
//

#include "FFmpegCore.h"
#include <jni.h>
#include "myjnihelper/android_log.h"


extern "C" {

#include "ffmpeg/ffmpeg.h"

const AVCodecID TARGET_IMAGE_CODEC = AV_CODEC_ID_PNG;
const AVPixelFormat TARGET_IMAGE_FORMAT = AV_PIX_FMT_RGBA; //AV_PIX_FMT_RGB24;


JNIEXPORT jint JNICALL
Java_com_caldremch_ffmpegcore_FFmpegCore_execCmd(JNIEnv *env, jclass type, jobjectArray cmds) {

    log_debug("调用开始");
    av_register_all();

    int argc = env->GetArrayLength(cmds);

    char *argv[argc];

    for (int i = 0; i < argc; ++i) {
        jstring js = (jstring) (env->GetObjectArrayElement(cmds, i));
        argv[i] = (char *) (env->GetStringUTFChars(js, 0));
        log_debug("命令:::JNI:%s", argv[i]);
    }
    int ret = jxRun(argc, argv);
    log_debug("命令:::返回:%d", ret);
    return ret;


} extern "C"
JNIEXPORT jbyteArray JNICALL
Java_com_caldremch_ffmpegcore_FFmpegCore_getFrameAt(JNIEnv *env, jobject jobject1, jstring path_,
                                                    jint timeUnit, jint option) {

    /**
     * 1.设置路径, 初始化数据源
     * 2.获取帧数据
     * 3.转为字节数据返回
     */

    AVPacket packet;
    av_init_packet(&packet);
    jbyteArray array = NULL;

    av_register_all();

    const char *pathChar = env->GetStringUTFChars(path_, NULL);

    //读取文件
    AVFormatContext *avFormatContext;
    AVInputFormat avInputFormat;

    avFormatContext = avformat_alloc_context();

    /*
     * ps：函数调用成功之后处理过的AVFormatContext结构体。
      file：打开的视音频流的URL。
      fmt：强制指定AVFormatContext中AVInputFormat的。这个参数一般情况下可以设置为NULL，这样FFmpeg可以自动检测AVInputFormat。
      dictionay：附加的一些选项，一般情况下可以设置为NULL。
    */
    int av = avformat_open_input(&avFormatContext, pathChar, NULL, NULL);
    if (av != 0) {
        log_error("视频打开失败");
        return NULL;
    }


    if (avformat_find_stream_info(avFormatContext, NULL) < 0) {

        log_error("获取视频流信息失败");
        //关闭打开的文件
        avformat_close_input(&avFormatContext);
        return NULL;
    }


    int video_index = -1;
    //获取视频第一帧
    for (int i = 0; i < avFormatContext->nb_streams; ++i) {

        if (avFormatContext->streams[i]->codec->codec_type == AVMEDIA_TYPE_VIDEO &&
            video_index < 0) {
            video_index = i;
            log_debug("第一帧为: %d", i);
        }

    }

    if (video_index != -1) {

        AVCodecContext *codecContext;
        AVCodec *codec;

        codecContext = avFormatContext->streams[video_index]->codec;

        //寻找编码器
        const AVCodecDescriptor *codecDescriptor =
                avcodec_descriptor_get(codecContext->codec_id);

        if (codecDescriptor) {
            log_debug("avcodec_descriptor_get:::::%s", codecDescriptor->name);
        }

        codec = avcodec_find_decoder(codecContext->codec_id);

        if (codec == NULL) {
            log_error("没有找到解码器 avcodec_find_decoder");
            return NULL;
        }

        if (avcodec_open2(codecContext, codec, NULL) < 0) {
            log_error("avcodec_open2错误");
            return NULL;
        }

        switch (codecContext->codec_type) {


            case AVMEDIA_TYPE_VIDEO:


                //查找目标编码器
                AVCodec *targetCodec = avcodec_find_encoder(TARGET_IMAGE_CODEC);
                AVStream *avStream = avFormatContext->streams[video_index];

                if (!targetCodec) {
                    log_error("错误 avcodec_find_encoder");
                    return NULL;
                }

                //目标上下文
                codecContext = avcodec_alloc_context3(targetCodec);
                if (!codecContext) {
                    log_error("错误 avcodec_alloc_context3");
                    return NULL;
                }


                codecContext->bit_rate = avStream->codec->bit_rate;
                codecContext->width = avStream->codec->width;
                codecContext->height = avStream->codec->height;
                codecContext->pix_fmt = TARGET_IMAGE_FORMAT;
                codecContext->codec_type = AVMEDIA_TYPE_VIDEO;
                codecContext->time_base.num = avStream->codec->time_base.num;
                codecContext->time_base.den = avStream->codec->time_base.den;

                //打开目标编码器
                if (avcodec_open2(codecContext, targetCodec, NULL) < 0) {
                    log_error("avcodec_open2 error");
                    return NULL;
                }


                SwsContext *swsContext = sws_getContext(
                        codecContext->width,
                        codecContext->height,
                        codecContext->pix_fmt,
                        codecContext->width,
                        codecContext->height,
                        TARGET_IMAGE_FORMAT,
                        SWS_BILINEAR,
                        NULL, NULL, NULL
                );


                int64_t seek_time = av_rescale_q(
                        timeUnit,
                        AV_TIME_BASE_Q,
                        avFormatContext->streams[video_index]->time_base);

                int64_t seek_stream_duration = avFormatContext->streams[video_index]->duration;


                int flags = 0;
                int ret = -1;

                if (seek_stream_duration > 0 && seek_time > seek_stream_duration) {
                    seek_time = seek_stream_duration;
                }

                if (seek_time < 0) {
                    log_error("seek_stream_duration为0检查");
                    return NULL;
                }



                //默认为0, 暂时不做处理

                ret = av_seek_frame(avFormatContext, video_index, seek_time, flags);


                log_error("av_seek_frame LLL ---> %d", ret);

                if (ret < 0){
                    log_error("av_seek_frame error");
                    return NULL;
                }

                if (video_index >= 0){
                    avcodec_flush_buffers(avStream->codec);
                }
                break;

        }


    }





    //读取视频帧, 成功则执行以下代码
//    int size =  packet.size;
//    uint8_t*data = packet.data;
//    array = env->NewByteArray(size);
//    if (!array){
//        log_debug("getFrameAt.错误");
//    }else{
//        jbyte *jbytes = env->GetByteArrayElements(array, NULL);
//
//        if (jbytes != NULL){
//            memcpy(jbytes, data, size);
//            env->ReleaseByteArrayElements(array, jbytes, 0);
//        }
//    }
//
//    av_packet_unref(&packet);

    return NULL;
}
}