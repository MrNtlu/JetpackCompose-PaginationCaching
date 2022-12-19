package com.mrntlu.jetpackcompose_paginationcaching.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class Article(
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "title") val title: String,
    @PrimaryKey(autoGenerate = false) val url: String,
)
