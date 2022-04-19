package com.keiven.hackernewtopstory.di.modules

import dagger.Module
import dagger.Provides
import com.keiven.hackernewtopstory.data.repository.StoryRepository
import com.keiven.hackernewtopstory.data.repository.StoryRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideStoryRepository(repository: StoryRepositoryImpl): StoryRepository {
        return repository
    }
}