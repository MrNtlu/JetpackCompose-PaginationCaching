package com.mrntlu.jetpackcompose_paginationcaching.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.*
import com.mrntlu.jetpackcompose_paginationcaching.models.Movie
import com.mrntlu.jetpackcompose_paginationcaching.repository.MoviesRemoteMediator
import com.mrntlu.jetpackcompose_paginationcaching.service.MoviesApiService
import com.mrntlu.jetpackcompose_paginationcaching.service.MoviesDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

const val PAGE_SIZE = 20

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesApiService: MoviesApiService,
    private val moviesDatabase: MoviesDatabase,
): ViewModel() {
    /**
     * A PagingSource still loads the data; but when the paged data is exhausted, the Paging library triggers the RemoteMediator to load new data from the network source.
     * The RemoteMediator stores the new data in the local database, so an in-memory cache in the ViewModel is unnecessary.
     * Finally, the PagingSource invalidates itself, and the Pager creates a new instance to load the fresh data from the database.
     */

    @OptIn(ExperimentalPagingApi::class)
    fun getPopularMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = 10,
                initialLoadSize = PAGE_SIZE, // How many items you want to load initially
            ),
            pagingSourceFactory = {
                // The pagingSourceFactory lambda should always return a brand new PagingSource
                // when invoked as PagingSource instances are not reusable.
                moviesDatabase.getMoviesDao().getMovies()
            },
            remoteMediator = MoviesRemoteMediator(
                moviesApiService,
                moviesDatabase,
            )
        ).flow
}