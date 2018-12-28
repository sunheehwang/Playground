package com.happy.playground.dagger.module

import com.happy.playground.MainActivity
import com.happy.playground.dagger.scope.PerActivity
import com.happy.playground.photos.photos.di.PhotosFragmentProvider
import com.happy.playground.photos.photos.di.PhotosModule
import com.happy.playground.photos.photo.di.PhotoFragmentProvider
import com.happy.playground.photos.photo.di.PhotoModule
import com.happy.playground.photos.photo.ui.PhotoActivity
import com.happy.playground.photos.photos.ui.PhotosActivity
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

    @PerActivity
    @ContributesAndroidInjector(modules = [PhotoModule::class, PhotoFragmentProvider::class])
    abstract fun bindPhotoActivity(): PhotoActivity
}