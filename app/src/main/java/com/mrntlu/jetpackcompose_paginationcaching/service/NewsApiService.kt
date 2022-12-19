package com.mrntlu.jetpackcompose_paginationcaching.service

import com.mrntlu.jetpackcompose_paginationcaching.models.NewsResponse
import com.mrntlu.jetpackcompose_paginationcaching.utils.Constants.API_KEY
import com.mrntlu.jetpackcompose_paginationcaching.viewmodels.PAGE_SIZE
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything?q=apple&sortBy=popularity&apiKey=${API_KEY}&pageSize=${PAGE_SIZE}")
    suspend fun getNews(
        @Query("page") page: Int
    ): NewsResponse
}