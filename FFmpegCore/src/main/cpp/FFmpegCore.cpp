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

    //AVPacket来暂存解复用之后、解码之前的媒体数据（一个音/视频帧、一个字幕包等）及附加信息（解码时间戳、显示时间戳、时长等）
    AVPacket packet;

    av_init_packet(&packet);

    jbyteArray array = NULL;

    av_register_all();

    const char *pathChar = env->GetStringUTFChars(path_, NULL);

    //描述了一个媒体文件或媒体流的构成和基本信息
    AVFormatContext *avFormatContext;

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

    //查找流信息
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

        //描述编解码器上下文的数据结构，包含了众多编解码器需要的参数信息
        AVCodecContext *codecContext;

        //采用的解码器AVCodec H.264,MPEG2
        AVCodec *codec;

        //codec获取视频流的编解码器
        codecContext = avFormatContext->streams[video_index]->codec;

        //获取该视频的编解码器的描述
        const AVCodecDescriptor *codecDescriptor =
                avcodec_descriptor_get(codecContext->codec_id);

        //打印
        if (codecDescriptor) {
            log_debug("avcodec_descriptor_get:::::%s", codecDescriptor->name);
        }

        //根据编解码上下文结构的 id, 查看视频所使用的解码器
        codec = avcodec_find_decoder(codecContext->codec_id);

        if (codec == NULL) {
            log_error("没有找到解码器 avcodec_find_decoder");
            return NULL;
        }

        //avcodec_open2用于初始化一个视音频编解码器的AVCodecContex
        //codecContext: 需要初始化的AVCodecContext。
        //codec: 输入的AVCodec解码器。
        //options：一些选项。例如使用libx264编码的时候，“preset”，“tune”等都可以通过该参数设置。
        //在解码之前需要调用
        if (avcodec_open2(codecContext, codec, NULL) < 0) {
            log_error("avcodec_open2错误");
            return NULL;
        }

        switch (codecContext->codec_type) {


            case AVMEDIA_TYPE_VIDEO:


                //查找为AV_CODEC_ID_PNG的编码器
                AVCodec *targetCodec = avcodec_find_encoder(TARGET_IMAGE_CODEC);

                //AVStream: 存储每一个视频/音频流信息的结构体
                AVStream *avStream = avFormatContext->streams[video_index];

                if (!targetCodec) {
                    log_error("错误 avcodec_find_encoder");
                    return NULL;
                } else {
                    log_debug("targetCodec:::%s, %d", targetCodec->name, targetCodec->id);
                }

                //配置一个编解码器, 这里传入targetCodec, 意为配置一个图片的编码器
                AVCodecContext *targetCodecCtx = avcodec_alloc_context3(targetCodec);
                if (!targetCodecCtx) {
                    log_error("错误 avcodec_alloc_context3");
                    return NULL;
                }


                targetCodecCtx->bit_rate = avStream->codec->bit_rate;
                targetCodecCtx->width = avStream->codec->width;
                targetCodecCtx->height = avStream->codec->height;
                targetCodecCtx->pix_fmt = TARGET_IMAGE_FORMAT;
                targetCodecCtx->codec_type = AVMEDIA_TYPE_VIDEO;
                targetCodecCtx->time_base.num = avStream->codec->time_base.num;
                targetCodecCtx->time_base.den = avStream->codec->time_base.den;

                //初始化目标编解码器(初始化目标编码器)
                if (avcodec_open2(targetCodecCtx, targetCodec, NULL) < 0) {
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
                //读取指定帧
                ret = av_seek_frame(avFormatContext, video_index, seek_time, flags);


                log_error("av_seek_frame LLL ---> %d", ret);

                if (ret < 0) {
                    log_error("av_seek_frame error");
                    return NULL;
                }

                if (video_index >= 0) {

                    //Should be called e.g. when seeking or when switching to a different stream.
                    //Reset the internal decoder state / flush internal buffers.
                    //重置内部解码器的状态, 在指定了指定了不同的流后
                    avcodec_flush_buffers(avStream->codec);
                }


                //new a video AVFrame
                AVFrame *frame = av_frame_alloc();

                //appointFrame 指定帧是否获得
                int *appointFrame = 0;

                if (!frame) {
                    log_error("av_frame_alloc");
                    return NULL;
                } else {
                    log_debug("frame alloc success");
                }

                //从容器中读取帧的数据
                while (av_read_frame(avFormatContext, &packet) >= 0) {


                    log_debug("av_read_frame::packet.stream_index::%d", packet.stream_index);

                    //判断是否在指定的帧
                    if (packet.stream_index == video_index) {

                        log_debug("av_read_frame 找到指定帧");

                        AVCodecID codec_id = avStream->codec->codec_id;
                        AVPixelFormat pix_fmt = avStream->codec->pix_fmt;

                        log_debug("AVCodecID codec_id--> %d", codec_id);
                        log_debug("AV_CODEC_ID_PNG codec_id--> %d", AV_CODEC_ID_PNG);
                        log_debug("AV_CODEC_ID_MJPEG codec_id--> %d", AV_CODEC_ID_MJPEG);
                        log_debug("AV_CODEC_ID_BMP codec_id--> %d", AV_CODEC_ID_BMP);
                        log_debug("AVPixelFormat pix_fmt--> %d", pix_fmt);
                        log_debug("AV_PIX_FMT_RGBA pix_fmt--> %d", AV_PIX_FMT_RGBA);
                        //判断是否是支持的格式
//                        if ((codec_id == AV_CODEC_ID_PNG || codec_id == AV_CODEC_ID_MJPEG
//                             || codec_id == AV_CODEC_ID_BMP
//                            ) && pix_fmt == AV_PIX_FMT_RGBA) {

                        log_debug("格式支持");
                        log_debug("开始解码视频帧");
                        //解码视频帧
//                            int decode_ret = avcodec_decode_video2(avStream->codec, frame,
//                                                                   appointFrame, &packet);


/*
 * 解码后图像空间由函数内部申请，你所做的只需要分配 AVFrame 对象空间，如果你每次调用avcodec_receive_frame传递同一个对象，接口内部会判断空间是否已经分配，如果没有分配会在函数内部分配。

avcodec_send_packet和avcodec_receive_frame调用关系并不一定是一对一的，比如一些音频数据一个AVPacket中包含了1秒钟的音频，调用一次avcodec_send_packet之后，可能需要调用25次 avcodec_receive_frame才能获取全部的解码音频数据，所以要做如下处理：
 因为一次avcodec_receive_frame可能无法接收到所有数据

 */
                        //发送编码数据包
                        int decode_ret = avcodec_send_packet(codecContext, &packet);

                        log_debug("avcodec_send_packet--->%d", decode_ret);

                        if (decode_ret != 0) {
                            log_error("avcodec_send_packet error");
                            return NULL;
                        }


                        //接收解码数据包
                        while (avcodec_receive_frame(codecContext, frame) == 0) {
                            //读取到一帧视频或者音频
                            //处理


                        }

                        break;



//                            if (decode_ret <= 0) {
//                                log_debug("解码视频帧错误");
//                                *appointFrame = 0;
//                                break;
//                            }
//
//                            log_debug("解码视频帧成功");
//
//
//
//                            //判断是否已经获得指定帧
//                            if (*appointFrame) {
//                                log_debug("找到指定视频帧");
//                                //如果原先 packet.data 存在数据, 先删除
//                                if (packet.data) {
//                                    log_debug("packet.data存在");
//                                    av_packet_unref(&packet);
//                                } else {
//                                    log_debug("packet.data不存在");
//                                }
//                                log_debug("重新初始化av_init_packet");
//                                //重新初始化
//                                av_init_packet(&packet);
//
//                                //开始转换成图片
//                            }
//                        }

                    }

                }

//                log_error("直接小于0??");

                //释放
                av_frame_free(&frame);

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