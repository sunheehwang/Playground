package com.happy.playground.dagger.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.happy.playground.BuildConfig
import com.happy.playground.repository.api.MockableApiService
import com.happy.playground.repository.api.MockableApiService.Companion.END_POINT
import com.happy.playground.repository.database.MockableDatabase
import com.happy.playground.repository.database.PhotoDao
import com.happy.playground.repository.infrastructure.PlaygroundRepository
import com.happy.playground.util.PlatformLogger
import com.happy.playground.util.TimberLogger
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
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

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            if (BuildConfig.DEBUG) {
                Timber.d(message)
            }
        })

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return interceptor
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(logger)
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun provideGson() : Gson {
        return GsonBuilder()

            .create()

    }

    @Provides
    @Singleton
    fun provideMockableDatabase(context: Context) : MockableDatabase {
        return MockableDatabase.getInstance(context)
    }

    @Provides
    @Singleton
    fun providePhotoDao(mockableDatabase: MockableDatabase):PhotoDao {
        return mockableDatabase.photoDao()
    }


    @Provides
    @Singleton
    fun provideMockableApiService(okHttpClient: OkHttpClient, gson: Gson): MockableApiService {
        return Retrofit.Builder()
            .baseUrl(END_POINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(MockableApiService::class.java)
    }
    @Provides
    @Singleton
    fun providePlaygroundRepository(mockableApiService: MockableApiService,photoDao: PhotoDao): PlaygroundRepository = PlaygroundRepository(mockableApiService, photoDao)

}