package com.keiven.hackernewtopstory.di.modules

import dagger.Module
import dagger.Provides
import com.keiven.hackernewtopstory.data.services.ApiService
import com.keiven.hackernewtopstory.helper.constants.Endpoint
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Endpoint.BASE_URL).build().create(ApiService::class.java)
    }
}