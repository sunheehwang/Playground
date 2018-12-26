package com.happy.playground.app

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import androidx.work.Worker
import com.happy.playground.BuildConfig
import com.happy.playground.dagger.DaggerAppComponent
import com.happy.playground.dagger.injector.HasWorkerInjector
import com.happy.playground.repository.database.MockableDatabase
import com.happy.playground.repository.infrastructure.PlaygroundRepository
import dagger.android.*
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

class PlaygroundApplication : Application(), HasActivityInjector, HasServiceInjector,
    HasBroadcastReceiverInjector, HasWorkerInjector {

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceDispatchingAndroidInjector: DispatchingAndroidInjector<Service>

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
        //context = WeakReference(applicationContext)
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(Timber.DebugTree())
            //Timber.plant(CrashlyticsTree())
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityDispatchingAndroidInjector

    override fun serviceInjector(): AndroidInjector<Service> = serviceDispatchingAndroidInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = broadcastReceiverInjector

    override fun workerInjector() = workerInjector

    /* companion object {
         var context: WeakReference<Context>? = null
         fun getAppContext(): Context? {
             return context?.get();
         }
     }*/
}