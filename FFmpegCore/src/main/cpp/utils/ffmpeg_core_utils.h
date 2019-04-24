//
// Created by Caldremch on 2019-04-24.
//

#ifndef ANDROIDVIDEOPLAYER_FFMPEG_CORE_UTILS_H
#define ANDROIDVIDEOPLAYER_FFMPEG_CORE_UTILS_H

#include <libavformat/avformat.h>
int frame2image( AVFrame *pAvFrame, char *outFileName);

#endif //ANDROIDVIDEOPLAYER_FFMPEG_CORE_UTILS_H
