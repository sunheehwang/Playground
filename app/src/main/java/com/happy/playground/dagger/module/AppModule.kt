package com.happy.playground.dagger.module

import android.app.Application
import android.content.Context
import com.happy.playground.BuildConfig
import com.happy.playground.repository.api.MockableApiService
import com.happy.playground.repository.api.MockableApiService.Companion.END_POINT
import com.happy.playground.repository.database.MockableDatabase
import com.happy.playground.repository.database.PhotoDao
import com.happy.playground.util.PlatformLogger
import com.happy.playground.util.PlaygroundSchedulers
import com.happy.playground.util.Schedulers
import com.happy.playground.util.TimberLogger
import com.happy.playground.util.typeadapter.*
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(AppJsonAdapterFactory.INSTANCE)
            //.add(TimestampAdapter(CalendarISO8601Converter()))
            .add(java.lang.Long::class.java, ISO8610Date::class.java, TimestampAdapter(CalendarISO8601Converter()))
            .add(Long::class.java, ISO8610Date::class.java, TimestampAdapter(CalendarISO8601Converter()))
            .add(java.lang.Long::class.java, EnsureLong::class.java, EnsuresLongAdapter())
            .add(Long::class.java, EnsureLong::class.java, EnsuresLongAdapter())
            .build()
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
    fun provideMockableApiService(okHttpClient: OkHttpClient, moshi: Moshi): MockableApiService {
        return Retrofit.Builder()
            .baseUrl(END_POINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
            .create(MockableApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideSchedulers(): Schedulers = PlaygroundSchedulers

/*

    @Provides
    @Singleton
    fun provideRepository(mockableApiService: MockableApiService,photoDao: PhotoDao, schedulers: Schedulers): Repository = PlaygroundRepository(mockableApiService, photoDao, schedulers)
*/

}