package com.example.ccp_aos_cleanarch.di

import com.example.data.network.ApiClient
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
// 싱글톤으로 생성
object ApiModule {

    private const val BASEURL: String = "https://api.chucknorris.io/"

    @Singleton
    @Provides
    fun create(): ApiClient {

        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASEURL)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(ApiClient::class.java)
    }

}