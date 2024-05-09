package com.task.xm.di

import com.task.xm.core.datasources.remote.XmRemoteDataSource
import com.task.xm.data.repository.QuestionsRepositoryImpl
import com.task.xm.domain.repositories.QuestionsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent

/**
 * @Author Abdullah Abo El~Makarem on 07/05/2024.
 */

@Module
@InstallIn(ActivityComponent::class)
class UseCaseModule {

}