package com.mrntlu.jetpackcompose_paginationcaching.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mrntlu.jetpackcompose_paginationcaching.models.Article
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsApiService
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsDatabase

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator (
    private val newsApiService: NewsApiService,
    private val newsDatabase: NewsDatabase,
): RemoteMediator<Int, Article>() {

    //To be continue...
    //https://developer.android.com/codelabs/android-paging#15
    //https://github.com/android/architecture-components-samples/tree/main/PagingSample/app/src/main/java/paging/android/example/com/pagingsample
    //https://developer.android.com/reference/kotlin/androidx/paging/RemoteMediator
    //https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data

    /***
     *
     * @param state This gives us information about the pages that were loaded before,
     * the most recently accessed index in the list, and the PagingConfig we defined when initializing the paging stream.
     * @param loadType this tells us whether we need to load data at the end (LoadType.APPEND)
     * or at the beginning of the data (LoadType.PREPEND) that we previously loaded,
     * or if this the first time we're loading data (LoadType.REFRESH).
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        TODO("Not yet implemented")
    }
}