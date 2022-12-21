package com.mrntlu.jetpackcompose_paginationcaching.service

import com.mrntlu.jetpackcompose_paginationcaching.models.MovieResponse
import com.mrntlu.jetpackcompose_paginationcaching.utils.Constants.MOVIE_API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApiService {
    @GET("movie/popular?api_key=${MOVIE_API_KEY}&language=en-US")
    suspend fun getPopularMovies(
        @Query("page") page: Int
    ): MovieResponse
}