//
// Created by Caldremch on 2019-04-27.
//

#ifndef ANDROIDVIDEOPLAYER_MUTEXLOCK_H
#define ANDROIDVIDEOPLAYER_MUTEXLOCK_H

#include <pthread.h>

class MutexLock {

public:
    MutexLock();
    virtual ~MutexLock();
    void lock();
    void unLock();
    int tryLock();
private:
    pthread_mutex_t mutex;
};


#endif //ANDROIDVIDEOPLAYER_MUTEXLOCK_H
