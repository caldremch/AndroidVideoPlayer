//
// Created by Caldremch on 2019-04-24.
//

#include "ffmpeg_core_utils.h"
#include "../myjnihelper/android_log.h"

int frame2image( AVFrame *pAvFrame, char *outFileName, int num , int den){


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

    pAVCodecContext->time_base = (AVRational){num,den};

    if (avcodec_open2(pAVCodecContext, pAVCodec, NULL) < 0){
        log_error("avcodec_open2 开打编解码器 失败");
        return -1;
    }else{
        log_error("avcodec_open2 开打编解码器 成功");
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

    avcodec_flush_buffers(pAVCodecContext);


    ret = avcodec_send_packet(pAVCodecContext, &pkt);



    if(ret < 0){
        log_error("avcodec_send_packet0 error %d", ret);
        log_error("avcodec_send_packet1 error %d", AVERROR_EOF);
        log_error("avcodec_send_packet2 error %d", AVERROR(EINVAL));
        log_error("avcodec_send_packet3 error %d", AVERROR(EAGAIN));
        log_error("avcodec_send_packet4 error %d", AVERROR(ENOMEM));
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

int frame2image2( AVFrame *pAvFrame, int width, int height, char *outFileName, int num , int den){




    AVFormatContext*output_ctx = avformat_alloc_context();

    avformat_alloc_output_context2(&output_ctx, NULL, "singlejpeg", outFileName);

    if(avio_open(&output_ctx->pb, outFileName, AVIO_FLAG_READ_WRITE)< 0){
        log_error("avio_open");
        return -1;
    }


    AVStream*stream = avformat_new_stream(output_ctx, NULL);

    if(stream== NULL){
        log_error("avformat_new_stream");
        return -1;
    }

    AVCodecContext *codec_ctx = stream->codec;

    codec_ctx->codec_id = output_ctx->oformat->video_codec;
    codec_ctx->codec_type = AVMEDIA_TYPE_VIDEO;
    codec_ctx->pix_fmt = AV_PIX_FMT_YUVJ420P;
    codec_ctx->height = height;
    codec_ctx->width = width;
    codec_ctx->time_base.num = 1;
    codec_ctx->time_base.den = 25;

    //打印输出文件信息
    av_dump_format(output_ctx, 0, outFileName, 1);

    AVCodec *codec = avcodec_find_encoder(codec_ctx->codec_id);

    if (!codec){
        log_error("avcodec_find_encoder");
        return -1;
    }

    if(avcodec_open2(codec_ctx, codec, NULL)< 0){
        log_error("avcodec_open2");
        return -1;
    }

    if(avcodec_parameters_from_context(stream->codecpar, codec_ctx)< 0){
        log_error("avcodec_parameters_from_context");
        return -1;
    }

    if( avformat_write_header(output_ctx, NULL)< 0){
        log_error("avformat_write_header");
        return -1;
    }

    int size  = codec_ctx->width * codec_ctx->height;

    AVPacket *packet;

    av_new_packet(packet, size*3);

    int got_picture = 0;

    int result = avcodec_encode_video2(codec_ctx, packet, pAvFrame, &got_picture);

    if (result < 0){
        log_error("avcodec_encode_video2");
        return -1;
    }

    log_debug("got picture %d", got_picture);

    if(got_picture == 1){
        result = av_write_frame(output_ctx, packet);
    }


    av_free_packet(packet);
    av_write_trailer(output_ctx);
    if(pAvFrame){
        av_frame_unref(pAvFrame);
    }

    avio_close(output_ctx->pb);
    avformat_free_context(output_ctx);

    return 0;
}