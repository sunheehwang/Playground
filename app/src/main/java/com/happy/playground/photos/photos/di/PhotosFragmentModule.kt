package com.happy.playground.photos.photos.di

import com.happy.playground.repository.Repository
import com.happy.playground.repository.api.MockableApiService
import com.happy.playground.repository.database.PhotoDao
import com.happy.playground.repository.infrastructure.PlaygroundRepository
import com.happy.playground.util.Schedulers
import dagger.Module
import dagger.Provides


@Module
class PhotosFragmentModule {
    @Provides
    fun provideRepository(
        mockableApiService: MockableApiService,
        photoDao: PhotoDao,
        schedulers: Schedulers
    ): Repository = PlaygroundRepository(mockableApiService, photoDao, schedulers)
}