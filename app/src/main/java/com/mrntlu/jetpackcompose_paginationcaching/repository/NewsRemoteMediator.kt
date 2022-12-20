package com.mrntlu.jetpackcompose_paginationcaching.repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mrntlu.jetpackcompose_paginationcaching.models.Article
import com.mrntlu.jetpackcompose_paginationcaching.models.NewsResponse
import com.mrntlu.jetpackcompose_paginationcaching.models.RemoteKeys
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsApiService
import com.mrntlu.jetpackcompose_paginationcaching.service.NewsDatabase
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator (
    private val newsApiService: NewsApiService,
    private val newsDatabase: NewsDatabase,
): RemoteMediator<Int, Article>() {

    /* Sources
    https://www.youtube.com/watch?v=dqj9aj-Z898&ab_channel=Stevdza-San
    https://www.youtube.com/watch?v=3yIIiaDN0CI&ab_channel=HimanshuGaur
    https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
    https://github.com/MrNtlu/PassVault/blob/master/app/src/main/java/com/mrntlu/PassVault/utils/NetworkBoundResource.kt

    To be continue...
    https://developer.android.com/topic/libraries/architecture/paging/v3-network-db
    https://developer.android.com/reference/kotlin/androidx/paging/RemoteMediator
    https://developer.android.com/topic/libraries/architecture/paging/v3-paged-data
    https://youtu.be/EpcIjXgg3e4
    https://github.com/googlecodelabs/android-paging/blob/main/advanced/end/app/src/main/java/com/example/android/codelabs/paging/data/GithubRemoteMediator.kt
    https://github.com/googlecodelabs/android-paging/blob/main/advanced/end/app/src/main/java/com/example/android/codelabs/paging/db/RepoDao.kt
     */

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    /** LoadType.Append
     * When we need to load data at the end of the currently loaded data set, the load parameter is LoadType.APPEND
     */
    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Article>): RemoteKeys? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { article ->
            newsDatabase.getRemoteKeysDao().getRemoteKeyArticleURL(articleURL = article.url)
        }
    }

    /** LoadType.Prepend
     * When we need to load data at the beginning of the currently loaded data set, the load parameter is LoadType.PREPEND
     */
    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Article>): RemoteKeys? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull() {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { article ->
            newsDatabase.getRemoteKeysDao().getRemoteKeyArticleURL(articleURL = article.url)
        }
    }

    /** LoadType.REFRESH
     * Gets called when it's the first time we're loading data, or when PagingDataAdapter.refresh() is called;
     * so now the point of reference for loading our data is the state.anchorPosition.
     * If this is the first load, then the anchorPosition is null.
     * When PagingDataAdapter.refresh() is called, the anchorPosition is the first visible position in the displayed list, so we will need to load the page that contains that specific item.
     */
    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Article>): RemoteKeys? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.url?.let { articleURL ->
                newsDatabase.getRemoteKeysDao().getRemoteKeyArticleURL(articleURL = articleURL)
            }
        }
    }

    /**
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
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                //New Query so clear the DB
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                if (prevKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                Log.d("MainTest", "load: $nextKey $remoteKeys")
                if (nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                nextKey
            }
        }
        try {
            val apiResponse = try {
                newsApiService.getNews(page = page)
            } catch (_: Exception) {
                NewsResponse(articles = emptyList(), status = "error", totalResults = 0)
            }

            val articles = apiResponse.articles
            val endOfPaginationReached = articles.isEmpty()

            Log.d("MainTest", "load: ${apiResponse.status}\n" +
                    "page: $page\n" +
                    "articles: ${articles.isEmpty()}\n" +
                    "endOf: $endOfPaginationReached\n" +
                    "loadType: $loadType")
            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH && articles.isNotEmpty()) { //New query so we can delete everything.
                    newsDatabase.getRemoteKeysDao().clearRemoteKeys()
                    newsDatabase.getNewsDao().clearAllArticles()
                }
                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = articles.map {
                    RemoteKeys(articleURL = it.url, prevKey, nextKey)
                }
                Log.d("MainTest", "load: $prevKey $nextKey")
                newsDatabase.getRemoteKeysDao().insertAll(keys)
                newsDatabase.getNewsDao().insertAll(articles)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            Log.d("MainTest", "Error: $error")
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            Log.d("MainTest", "Error 2: $error")
            return MediatorResult.Error(error)
        }
    }
}