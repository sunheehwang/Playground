package com.happy.playground.app

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import androidx.work.Worker
import com.happy.playground.dagger.DaggerAppComponent
import com.happy.playground.dagger.injector.HasWorkerInjector
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import javax.inject.Inject

class PlaygroundApplication : Application(), HasActivityInjector,
    HasBroadcastReceiverInjector, HasWorkerInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    lateinit var workerInjector: DispatchingAndroidInjector<Worker>


    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .build()
            .inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity>  = activityDispatchingAndroidInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverInjector

    override fun workerInjector() = workerInjector

}