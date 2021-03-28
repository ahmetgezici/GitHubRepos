package com.ahmetgezici.githubrepos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmetgezici.githubrepos.model.FavoritesRepo
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(entities = [FavoritesRepo::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao?

    companion object{

        var instance: FavoritesDatabase? = null

        private const val NUMBER_OF_THREADS = 4
        val databaseExecutor: ExecutorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS)

        @Synchronized
        fun getDatabase(context: Context): FavoritesDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDatabase::class.java,
                    "favorites_database"
                ).fallbackToDestructiveMigration()
                    .build()
            }

            return instance as FavoritesDatabase
        }

    }



}