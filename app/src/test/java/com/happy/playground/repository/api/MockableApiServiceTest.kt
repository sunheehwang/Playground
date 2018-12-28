package com.happy.playground.repository.api

import android.content.Context
import com.happy.playground.repository.api.MockableApiService.Companion.END_POINT
import com.happy.playground.util.PlatformLogger
import com.happy.playground.util.typeadapter.*
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class MockableApiServiceTest {

    private lateinit var context: Context
    private lateinit var mockableApiService: MockableApiService
    private lateinit var logger: PlatformLogger

    @Before
    fun setUp() {

        context = mock<Context>(Context::class.java)
        //logger = PlatformLogger
        mockableApiService = Retrofit.Builder()
            .baseUrl(END_POINT)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(AppJsonAdapterFactory.INSTANCE)
                        //.add(TimestampAdapter(CalendarISO8601Converter()))
                        .add(
                            java.lang.Long::class.java,
                            ISO8610Date::class.java,
                            TimestampAdapter(CalendarISO8601Converter())
                        )
                        .add(Long::class.java, ISO8610Date::class.java, TimestampAdapter(CalendarISO8601Converter()))
                        .add(java.lang.Long::class.java, EnsureLong::class.java, EnsuresLongAdapter())
                        .add(Long::class.java, EnsureLong::class.java, EnsuresLongAdapter())
                        .build()
                )
            )
            .client(provideOkHttpClient(provideHttpLoggingInterceptor()))
            .build()
            .create(MockableApiService::class.java)
    }

    fun provideOkHttpClient(logger: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(logger)
            connectTimeout(15, TimeUnit.SECONDS)
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(15, TimeUnit.SECONDS)
        }.build()
    }

    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
            println(message)
        })

        interceptor.level = HttpLoggingInterceptor.Level.HEADERS
        return interceptor
    }
    @Test
    fun getPhotos() {
        mockableApiService.getPhotos()
            .subscribe({
                println("$it")
            }, {
                println("error $it")
            })
    }

}