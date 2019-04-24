//
// Created by Caldremch on 2019-04-24.
//

#include "ffmpeg_core_utils.h"
#include "../myjnihelper/android_log.h"

int frame2image( AVFrame *pAvFrame, char *outFileName){


    log_debug("开始转换");
    int width = pAvFrame->width;
    int height = pAvFrame->height;

    AVCodecContext *pAVCodecContext = NULL;

    //申请上下文
    AVFormatContext *pAVFormatContext = avformat_alloc_context();

    //oformat: The output container format
    //av_guess_format : Return the output format in the list of registered output formats
    //which best matches the provided parameters
    pAVFormatContext->oformat = av_guess_format("mjpeg", NULL, NULL);

    if (pAVFormatContext->oformat == NULL){
        log_error("执行失败： av_guess_format");
        return -1;
    }
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


    //采用的解码器AVCodec H.264,MPEG2
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

    pAVCodecContext->time_base = (AVRational){1,25};

    if (avcodec_open2(pAVCodecContext, pAVCodec, NULL) < 0){
        log_error("avcodec_open2 开打编解码器 失败");
        return -1;
    }

    int ret = avformat_write_header(pAVFormatContext, NULL);

    if (ret < 0){
        log_error("avformat_write_header error");
        return -1;
    }

    int y_size = width*height; //像素集合？

    AVPacket pkt;

    //更AVPacket分配足够大的空间
    av_new_packet(&pkt, y_size*3);


    ret = avcodec_send_packet(pAVCodecContext, &pkt);

    if(ret < 0){
        log_error("avcodec_send_packet error");
        return -1;
    }

    ret = avcodec_receive_frame(pAVCodecContext,  pAvFrame);

    if (ret < 0){
        log_error("avcodec_receive_frame error");
        return -1;
    }


    if(av_write_frame(pAVFormatContext, &pkt) <0){
        log_error("av_write_frame error");
        return -1;
    }

    av_packet_unref(&pkt);


    av_write_trailer(pAVFormatContext);

    avcodec_close(pAVCodecContext);
    avio_close(pAVFormatContext->pb);
    avformat_free_context(pAVFormatContext);

    log_debug("转换成功");

    return 0;
}