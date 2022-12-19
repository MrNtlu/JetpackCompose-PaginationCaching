package com.mrntlu.jetpackcompose_paginationcaching.service

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrntlu.jetpackcompose_paginationcaching.models.Article

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<Article>)

    @Query("Select * From article")
    fun getArticles(): PagingSource<Int, Article>

    @Query("Delete From article")
    suspend fun clearAllArticles()
}