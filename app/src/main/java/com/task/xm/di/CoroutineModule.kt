package com.task.xm.di

import com.task.xm.core.coroutines.CoroutineDispatcherProvider
import com.task.xm.core.coroutines.DefaultCoroutineDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */

@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {

    @Singleton
    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcherProvider = DefaultCoroutineDispatcherProvider()
}