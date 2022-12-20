package com.mrntlu.jetpackcompose_paginationcaching.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.mrntlu.jetpackcompose_paginationcaching.models.Article
import com.mrntlu.jetpackcompose_paginationcaching.repository.NewsPagingSource
import com.mrntlu.jetpackcompose_paginationcaching.repository.NewsRemoteMediator
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsApiService
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val PAGE_SIZE = 15

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsDatabase: NewsDatabase,
): ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    fun getNews(): Flow<PagingData<Article>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 5,
            ),
            pagingSourceFactory = {
                // The pagingSourceFactory lambda should always return a brand new PagingSource
                // when invoked as PagingSource instances are not reusable.
                newsDatabase.getNewsDao().getArticles()
            },
            remoteMediator = NewsRemoteMediator(
                newsApiService,
                newsDatabase,
            )
        ).flow.cachedIn(viewModelScope)
}