package com.mrntlu.jetpackcompose_paginationcaching.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrntlu.jetpackcompose_paginationcaching.models.RemoteKeys

@Dao
interface RemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("Select * From remote_key Where articleURL = :articleURL")
    suspend fun getRemoteKeyArticleURL(articleURL: String): RemoteKeys?

    @Query("Delete From remote_key")
    suspend fun clearRemoteKeys()
}