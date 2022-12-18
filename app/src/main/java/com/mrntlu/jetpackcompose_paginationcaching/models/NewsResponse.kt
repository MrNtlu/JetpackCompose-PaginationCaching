package com.mrntlu.jetpackcompose_paginationcaching.models

data class NewsResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)