package com.mrntlu.jetpackcompose_paginationcaching

import android.content.Context
import androidx.room.Room
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsApiService
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsDao
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsDatabase
import com.mrntlu.jetpackcompose_paginationcaching.service.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SingletonModule {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): NewsApiService =
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room
            .databaseBuilder(context, NewsDatabase::class.java, "news_database")
            .build()

    @Singleton
    @Provides
    fun provideNewsDao(newsDatabase: NewsDatabase): NewsDao = newsDatabase.getNewsDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(newsDatabase: NewsDatabase): RemoteKeysDao = newsDatabase.getRemoteKeysDao()
}