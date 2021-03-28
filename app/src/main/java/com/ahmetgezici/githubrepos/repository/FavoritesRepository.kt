package com.ahmetgezici.githubrepos.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.ahmetgezici.githubrepos.database.FavoritesDao
import com.ahmetgezici.githubrepos.database.FavoritesDatabase
import com.ahmetgezici.githubrepos.model.FavoritesRepo

class FavoritesRepository(application: Application) {

    private var favoritesDao: FavoritesDao? = null

    init {

        val favoritesDatabase = FavoritesDatabase.getDatabase(application)
        favoritesDao = favoritesDatabase.favoritesDao()

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun addFavoriteRepoDB(favoritesRepo: FavoritesRepo?) {

        FavoritesDatabase.databaseExecutor.execute(Runnable {

            favoritesDao?.addFavoriteRepo(favoritesRepo)

        })

    }

    ///////////////

    fun getFavoritesDB(): LiveData<List<FavoritesRepo>>? {

        return favoritesDao?.getFavorites()

    }

    //////////////

    fun deleteAllFavoritesDB() {

        FavoritesDatabase.databaseExecutor.execute(Runnable {

            favoritesDao?.deleteAllFavorites()

        })

    }

    ///////////////

    fun deleteFavoriteDB(id: Int?) {

        FavoritesDatabase.databaseExecutor.execute(Runnable {

            favoritesDao?.deleteFavorite(id)

        })

    }
}