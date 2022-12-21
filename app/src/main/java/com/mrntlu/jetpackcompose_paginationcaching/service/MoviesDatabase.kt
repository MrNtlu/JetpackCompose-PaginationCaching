package com.mrntlu.jetpackcompose_paginationcaching.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrntlu.jetpackcompose_paginationcaching.models.Movie
import com.mrntlu.jetpackcompose_paginationcaching.models.RemoteKeys

@Database(
    entities = [Movie::class, RemoteKeys::class],
    version = 1,
)
abstract class MoviesDatabase: RoomDatabase() {
    abstract fun getMoviesDao(): MoviesDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}