//
// Created by Caldremch on 2019-04-28.
//

#ifndef ANDROIDVIDEOPLAYER_VIDEOMANAGER_H
#define ANDROIDVIDEOPLAYER_VIDEOMANAGER_H
#include "myjnihelper/MutexLock.h"

class VideoManager {

public:
    /**
     * 第几秒获取帧
     * @param i
     */
    void getFrameAtTime(int i);
private:
    MutexLock mutex;
};


#endif //ANDROIDVIDEOPLAYER_VIDEOMANAGER_H
