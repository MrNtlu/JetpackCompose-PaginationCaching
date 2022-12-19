package com.mrntlu.jetpackcompose_paginationcaching.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mrntlu.jetpackcompose_paginationcaching.models.Article
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsApiService
import kotlinx.coroutines.delay

/***
 * Defines the source of data and how to retrive data from that source.
 * It can load data from any single source, including network sources and local databases.
 * For more details, https://developer.android.com/reference/kotlin/androidx/paging/PagingSource
 */
class NewsPagingSource(
    private val newsApiService: NewsApiService,
): PagingSource<Int, Article>() {

    /***
     * The refresh key is used for subsequent refresh calls to PagingSource.load() (the first call is initial load which uses initialKey provided by Pager).
     * A refresh happens whenever the Paging library wants to load new data to replace the current list,
     * e.g., on swipe to refresh or on invalidation due to database updates, config changes, process death, etc.
     */
    override fun getRefreshKey(state: PagingState<Int, Article>): Int {
        return ((state.anchorPosition ?: 1) - state.config.initialLoadSize / 2).coerceAtLeast(1)
    }

    /***
     * The load() function will be called by the Paging library to asynchronously fetch more data to be displayed as the user scrolls around.
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1

            delay(1000L) //TODO For better testing

            val response = newsApiService.getNews(page = page)

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (response.articles.isNotEmpty()) page + 1 else null

            LoadResult.Page(
                data = response.articles,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (error: Exception) {
            LoadResult.Error(error)
        }
    }
}