package com.happy.playground.dagger.injector

import androidx.work.Worker
import dagger.android.AndroidInjector

interface HasWorkerInjector {
    fun workerInjector() : AndroidInjector<Worker>
}