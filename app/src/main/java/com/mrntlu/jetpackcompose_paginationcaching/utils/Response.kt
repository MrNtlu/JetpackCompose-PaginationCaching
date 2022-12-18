package com.mrntlu.jetpackcompose_paginationcaching.utils

sealed class Response<out T> {
    object Idle: Response<Nothing>()

    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T?
    ): Response<T>()

    data class Failure(
        val errorMessage: String
    ): Response<Nothing>()
}
