package com.happy.playground.repository.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "date_taken")
    val date_taken: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "height")
    val height: String?,

    @ColumnInfo(name = "width")
    val width: String?
)