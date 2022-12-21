package com.mrntlu.jetpackcompose_paginationcaching.service

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrntlu.jetpackcompose_paginationcaching.models.Movie

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<Movie>)

    @Query("Select * From movies Order By page")
    fun getMovies(): PagingSource<Int, Movie>

    @Query("Delete From movies")
    suspend fun clearAllMovies()
}