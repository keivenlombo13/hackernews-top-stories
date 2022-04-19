package com.keiven.hackernewtopstory.di

import dagger.Component
import com.keiven.hackernewtopstory.di.modules.ApiModule
import com.keiven.hackernewtopstory.di.modules.RepositoryModule
import com.keiven.hackernewtopstory.di.modules.ViewModelFactoryModule
import com.keiven.hackernewtopstory.ui.storydetail.StoryDetailActivity
import com.keiven.hackernewtopstory.ui.topstory.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, RepositoryModule::class, ViewModelFactoryModule::class])
interface AppComponent {
    fun inject(activity: StoryDetailActivity)

    fun inject(activity: MainActivity)
}