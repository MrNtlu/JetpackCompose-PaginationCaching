package com.mrntlu.jetpackcompose_paginationcaching

import android.content.Context
import androidx.room.Room
import com.mrntlu.jetpackcompose_paginationcaching.service.MoviesApiService
import com.mrntlu.jetpackcompose_paginationcaching.service.MoviesDao
import com.mrntlu.jetpackcompose_paginationcaching.service.MoviesDatabase
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
    fun provideRetrofitInstance(): MoviesApiService =
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApiService::class.java)

    @Singleton
    @Provides
    fun provideMovieDatabase(@ApplicationContext context: Context): MoviesDatabase =
        Room
            .databaseBuilder(context, MoviesDatabase::class.java, "movies_database")
            .build()

    @Singleton
    @Provides
    fun provideMoviesDao(moviesDatabase: MoviesDatabase): MoviesDao = moviesDatabase.getMoviesDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(moviesDatabase: MoviesDatabase): RemoteKeysDao = moviesDatabase.getRemoteKeysDao()
}