package com.happy.playground.photos.photos.di

import com.happy.playground.photos.photos.ui.PhotosFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PhotosFragmentProvider {
    @ContributesAndroidInjector(modules = [PhotosFragmentModule::class])
    abstract fun providePhotosFragment() : PhotosFragment
}