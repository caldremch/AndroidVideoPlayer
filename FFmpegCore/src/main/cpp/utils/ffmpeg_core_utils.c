//
// Created by Caldremch on 2019-04-24.
//

#include "ffmpeg_core_utils.h"
#include "../myjnihelper/android_log.h"

int frame2image(AVFrame *pAvFrame, char *outFileName){

    int width = pAvFrame->width;
    int height = pAvFrame->height;

    AVCodecContext *pAVCodecContext = NULL;

    //申请上下文
    AVFormatContext *pAVFormatContext = avformat_alloc_context();

    //oformat: The output container format
    //av_guess_format : Return the output format in the list of registered output formats
    //which best matches the provided parameters
    pAVFormatContext->oformat = av_guess_format("png", NULL, NULL);

    /**
     * pb:I/O context
     *
     */
    if(avio_open(&pAVFormatContext->pb, outFileName, AVIO_FLAG_READ_WRITE) < 0){
        log_error("打开视频文件失败");
        return -1;
    }

    AVStream *pNewAVStream = avformat_new_stream(pAVFormatContext, 0);

    if (pNewAVStream == NULL){
        log_error("avformat_new_stream --> error");
        return -1;
    }

    //获取编解码器相关参数
    AVCodecParameters *parameters = pNewAVStream->codecpar;
    parameters->codec_id = pAVFormatContext->oformat->video_codec;
    parameters->codec_type = AVMEDIA_TYPE_VIDEO;
    parameters->format = AV_PIX_FMT_YUVJ420P;
    parameters->width = width;
    parameters->height = height;

    AVCodec *pAVCodec = avcodec_find_encoder(pNewAVStream->codecpar->codec_id);

    if (!pAVCodec){
        log_error("avcodec_find_encoder --> 找不到编码器");
        return -1;
    }

    pAVCodecContext = avcodec_alloc_context3(pAVCodec);

    if (!pAVCodecContext){
        log_error("avcodec_alloc_context3 失败");
        return -1;
    }

    //将 AVCodecContext 的成员复制到 AVCodecParameters结构体
    if(avcodec_parameters_to_context(pAVCodecContext, pNewAVStream->codecpar) < 0){
        log_error("复制到AVCodecParameters结构体失败: %s", av_get_media_type_string(AVMEDIA_TYPE_VIDEO));
        return -1;
    }

//    pAVCodecContext->time_base = (AVRational){pAvFrame};


}