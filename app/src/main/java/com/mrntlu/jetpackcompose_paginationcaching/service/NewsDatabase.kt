package com.mrntlu.jetpackcompose_paginationcaching.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrntlu.jetpackcompose_paginationcaching.models.Article
import com.mrntlu.jetpackcompose_paginationcaching.models.RemoteKeys

@Database(
    entities = [Article::class, RemoteKeys::class],
    version = 1,
)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
}