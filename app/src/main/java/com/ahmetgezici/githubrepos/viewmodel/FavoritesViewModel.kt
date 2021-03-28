package com.ahmetgezici.githubrepos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.ahmetgezici.githubrepos.model.FavoritesRepo
import com.ahmetgezici.githubrepos.repository.FavoritesRepository

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FavoritesRepository(application)

    ////////////////////////////////////////////////////////////////////////////////////////////////

    // Save Favorite to Database

    fun addFavoriteRepoDB(favoritesRepo: FavoritesRepo?) {
        repository.addFavoriteRepoDB(favoritesRepo)
    }

    ////////////////

    // Get Favorites From Database

    fun getFavoritesDB(): LiveData<List<FavoritesRepo>>? {
        return repository.getFavoritesDB()
    }

    ////////////////

    // Delete All Favorite from Database

    fun deleteAllFavoritesDB() {
        repository.deleteAllFavoritesDB()
    }

    ////////////////

    // Delete Favorite from Database

    fun deleteFavoriteDB(id: Int?) {
        repository.deleteFavoriteDB(id)
    }

}