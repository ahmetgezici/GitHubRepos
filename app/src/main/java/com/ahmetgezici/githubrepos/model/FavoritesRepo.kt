package com.ahmetgezici.githubrepos.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_repo")
data class FavoritesRepo(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uid")
    val uid: Int = 0,

    @ColumnInfo(name = "userName")
    val userName: String ,

    @ColumnInfo(name = "repoName")
    val repoName: String,

    @Nullable
    @ColumnInfo(name = "description")
    val description: String?,

    @Nullable
    @ColumnInfo(name = "language")
    val language: String?,

    @ColumnInfo(name = "starCount")
    val starCount: Int,

    @ColumnInfo(name = "createdDate")
    val createdDate: String

)
