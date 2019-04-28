//
// Created by Caldremch on 2019-04-27.
//

#include "MutexLock.h"

MutexLock::MutexLock() {
    pthread_mutex_init(&mutex, NULL);
}

MutexLock::~MutexLock() {
    pthread_mutex_destroy(&mutex);
}

void MutexLock::lock() {
    pthread_mutex_lock(&mutex);
}

void MutexLock::unLock() {
    pthread_mutex_unlock(&mutex);
}

int MutexLock::tryLock() {
    return pthread_mutex_trylock(&mutex);
}

