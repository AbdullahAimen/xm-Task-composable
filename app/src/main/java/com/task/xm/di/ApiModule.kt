package com.task.xm.di

import android.content.Context
import android.util.Log
import com.task.xm.BuildConfig
import com.task.xm.core.datasources.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

/**
 * @Author Abdullah Abo El~Makarem on 07/05/2024.
 */

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    private val DISK_CACHE_SIZE = 10 * 1024 * 1024 // 10 MB
    private val BASE_URL = "https://xm-assignment.web.app"

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }


    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()

        // Set Cache
        val cache: Cache? = initCache(context)
        cache?.let {
            builder.cache(it)
        }


        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }


    private fun initCache(context: Context): Cache? {
        try {
            var cacheDir = context.externalCacheDir
            if (cacheDir == null) {
                // Fall back to use the internal cache directory
                cacheDir = context.cacheDir
            }
            return cacheDir?.let { Cache(it, DISK_CACHE_SIZE.toLong()) }
        } catch (e: Exception) {
            Log.e("ApiModule", "Error creating OkHttp cache: " + e.message)
            return null
        }
    }
}