//
// Created by Caldremch on 2019-04-24.
//

#include "ffmpeg_core_utils.h"
#include <libavformat/>
#include "../myjnihelper/android_log.h"

int frame2image(AVFrame *pAvFrame, char *outFileName){

    int width = pAvFrame->width;
    int height = pAvFrame->height;

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
        return -1;
    }

    //获取编解码器相关参数
    AVCodecParameters *parameters = pNewAVStream->codecpar;



}