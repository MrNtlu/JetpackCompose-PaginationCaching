package com.mrntlu.jetpackcompose_paginationcaching.service

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrntlu.jetpackcompose_paginationcaching.models.Article

@Database(
    entities = [Article::class],
    version = 1,
)
abstract class NewsDatabase: RoomDatabase() {
    abstract fun getNewsDao(): NewsDao
}