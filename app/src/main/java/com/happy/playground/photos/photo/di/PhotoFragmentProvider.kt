package com.happy.playground.photos.photo.di

import com.happy.playground.photos.photo.ui.PhotoFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class PhotoFragmentProvider {
    @ContributesAndroidInjector(modules = [PhotoFragmentModule::class])
    abstract fun providePhotoFragment() : PhotoFragment
}