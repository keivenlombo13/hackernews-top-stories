package com.keiven.hackernewtopstory.di.modules

import dagger.Module
import dagger.Provides
import com.keiven.hackernewtopstory.data.repository.StoryRepository
import com.keiven.hackernewtopstory.data.repository.StoryRepositoryImpl
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideStoryRepository(repository: StoryRepositoryImpl): StoryRepository {
        return repository
    }
}