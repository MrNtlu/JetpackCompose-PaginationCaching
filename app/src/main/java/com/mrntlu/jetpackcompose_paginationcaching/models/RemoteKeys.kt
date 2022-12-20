package com.mrntlu.jetpackcompose_paginationcaching.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/***
 * When we get the last item loaded from the PagingState, there's no way to know the index of the page it belonged to.
 * To solve this problem, we can add another table that stores the next and previous page keys for each Article.
 */
@Entity(tableName = "remote_key")
data class RemoteKeys(
    @PrimaryKey
    val articleURL: String,
    val prevKey: Int?,
    val nextKey: Int?,
)
