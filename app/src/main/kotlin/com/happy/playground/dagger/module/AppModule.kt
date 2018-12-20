package com.happy.playground.dagger.module

import android.app.Application
import android.content.Context
import com.happy.playground.util.PlatformLogger
import com.happy.playground.util.TimberLogger
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    fun providePlatformLogger(): PlatformLogger {
        return TimberLogger
    }


}