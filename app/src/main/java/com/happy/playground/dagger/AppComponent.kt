package com.happy.playground.dagger

import android.app.Application
import com.happy.playground.app.PlaygroundApplication
import com.happy.playground.dagger.module.ActivityBuilder
import com.happy.playground.dagger.module.AndroidWorkerInjectionModule
import com.happy.playground.dagger.module.AppModule
import com.happy.playground.dagger.module.ReceiverBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class,
        AppModule::class, ActivityBuilder::class, ReceiverBuilder::class, AndroidWorkerInjectionModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: PlaygroundApplication)

    //fun inject(service: MessageService)
}