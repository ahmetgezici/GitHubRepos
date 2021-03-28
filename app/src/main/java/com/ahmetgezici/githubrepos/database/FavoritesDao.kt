package com.ahmetgezici.githubrepos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ahmetgezici.githubrepos.model.FavoritesRepo

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteRepo(favoritesRepo: FavoritesRepo?)

    @Query("SELECT * FROM favorites_repo")
    fun getFavorites(): LiveData<List<FavoritesRepo>>

    @Query("DELETE FROM favorites_repo")
    fun deleteAllFavorites()

    @Query("DELETE FROM favorites_repo WHERE uid=:uid")
    fun deleteFavorite(uid: Int?)

}