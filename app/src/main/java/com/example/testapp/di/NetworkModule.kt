package com.example.testapp.di

import com.example.testapp.BuildConfig
import com.example.testapp.common.network.interceptor.CurlLoggingInterceptor
import com.example.testapp.common.network.interceptor.ErrorInterceptor
import com.example.testapp.user.data.remote.UsersApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(json: Json, okHttp: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            baseUrl(BuildConfig.API_URL)
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            client(okHttp)
        }.build()
    }

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UsersApi {
        return retrofit.create(UsersApi::class.java)
    }

    @Provides
    @Singleton
    fun provideInterceptorOkHttpClient(
        errorInterceptor: ErrorInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        curlLoggingInterceptor: CurlLoggingInterceptor
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(errorInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(curlLoggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY
            else Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
    }

    @Provides
    @Singleton
    fun provideCurlLoggingInterceptor(): CurlLoggingInterceptor {
        return CurlLoggingInterceptor()
    }

    @Provides
    @Singleton
    fun provideErrorInterceptor(): ErrorInterceptor {
        return ErrorInterceptor()
    }
}