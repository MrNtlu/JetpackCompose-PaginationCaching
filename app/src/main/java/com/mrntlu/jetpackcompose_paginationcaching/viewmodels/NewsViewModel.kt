package com.mrntlu.jetpackcompose_paginationcaching.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mrntlu.jetpackcompose_paginationcaching.models.Article
import com.mrntlu.jetpackcompose_paginationcaching.repository.NewsPagingSource
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val PAGE_SIZE = 15

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsApiService: NewsApiService,
): ViewModel() {

    fun getNews(): Flow<PagingData<Article>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
            ),
            pagingSourceFactory = {
                // The pagingSourceFactory lambda should always return a brand new PagingSource
                // when invoked as PagingSource instances are not reusable.
                NewsPagingSource(newsApiService)
            }
        ).flow.cachedIn(viewModelScope)
}