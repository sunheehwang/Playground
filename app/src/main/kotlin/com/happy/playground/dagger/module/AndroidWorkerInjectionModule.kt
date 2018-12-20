package com.happy.playground.dagger.module

import androidx.work.Worker
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.Multibinds

@Module
abstract class AndroidWorkerInjectionModule {
    @Multibinds
    abstract fun workerInjectorFactories(): Map<Class<out Worker>, AndroidInjector.Factory<out Worker>>
}