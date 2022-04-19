package com.keiven.hackernewtopstory.di.modules

import dagger.Module
import dagger.Provides
import com.keiven.hackernewtopstory.ui.base.BaseViewModelFactory
import com.keiven.hackernewtopstory.ui.storydetail.StoryDetailViewModel
import com.keiven.hackernewtopstory.ui.topstory.MainViewModel
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {
    @Provides
    @Singleton
    fun provideStoryDetailViewModelFactory(viewModel: StoryDetailViewModel): BaseViewModelFactory<StoryDetailViewModel> {
        return BaseViewModelFactory { viewModel }
    }

    @Provides
    @Singleton
    fun provideMainViewModelFactory(viewModel: MainViewModel): BaseViewModelFactory<MainViewModel> {
        return BaseViewModelFactory { viewModel }
    }
}