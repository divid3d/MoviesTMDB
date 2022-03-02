package com.example.moviesapp.di

import android.content.Context
import androidx.room.Room
import com.example.moviesapp.db.FavouritesDatabase
import com.example.moviesapp.db.RecentlyBrowsedDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object DatabaseTestModule {

    @Provides
    @Named("test_favourite_database")
    fun provideInMemoryFavouriteDatabase(
        @ApplicationContext context: Context
    ): FavouritesDatabase = Room.inMemoryDatabaseBuilder(context, FavouritesDatabase::class.java)
        .allowMainThreadQueries()
        .build()

    @Provides
    @Named("test_recently_browsed_database")
    fun provideInMemoryRecentlyBrowsedDatabase(
        @ApplicationContext context: Context
    ): RecentlyBrowsedDatabase =
        Room.inMemoryDatabaseBuilder(context, RecentlyBrowsedDatabase::class.java)
            .allowMainThreadQueries()
            .build()

}