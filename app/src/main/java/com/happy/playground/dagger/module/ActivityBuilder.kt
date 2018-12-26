package com.happy.playground.dagger.module

import com.happy.playground.MainActivity
import com.happy.playground.dagger.scope.PerActivity
import com.happy.playground.photos.di.PhotosFragmentProvider
import com.happy.playground.photos.di.PhotosModule
import com.happy.playground.photos.ui.PhotosActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [])
    abstract fun bindMainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector(modules = [PhotosModule::class, PhotosFragmentProvider::class])
    abstract fun bindPhotosActivity(): PhotosActivity
}