package com.happy.playground.dagger

import android.content.Context
import com.happy.playground.dagger.module.LocalModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [LocalModule::class])
interface LocalComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(applicationContext: Context): Builder

        fun build(): LocalComponent
    }

}