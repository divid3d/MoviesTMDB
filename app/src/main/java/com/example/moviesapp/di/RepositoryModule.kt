package com.example.moviesapp.di

import android.content.Context
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.ConfigDataSource
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepositoryImpl
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.config.ConfigRepositoryImpl
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.repository.favourites.FavouritesRepositoryImpl
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.repository.movie.MovieRepositoryImpl
import com.example.moviesapp.repository.person.PersonRepository
import com.example.moviesapp.repository.person.PersonRepositoryImpl
import com.example.moviesapp.repository.search.SearchRepository
import com.example.moviesapp.repository.search.SearchRepositoryImpl
import com.example.moviesapp.repository.season.SeasonRepository
import com.example.moviesapp.repository.season.SeasonRepositoryImpl
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.repository.tv.TvSeriesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideConfigDataSource(
        @ApplicationContext context: Context,
        externalScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        apiHelper: TmdbApiHelper
    ): ConfigDataSource = ConfigDataSource(
        context = context,
        externalScope = externalScope,
        defaultDispatcher = dispatcher,
        apiHelper = apiHelper
    )

    @Module
    @InstallIn(SingletonComponent::class)
    interface RepositoryBinds {
        @Binds
        @Singleton
        fun provideConfigRepository(impl: ConfigRepositoryImpl): ConfigRepository

        @Binds
        @Singleton
        fun bindMovieRepository(impl: MovieRepositoryImpl): MovieRepository

        @Binds
        @Singleton
        fun bindTvSeriesRepository(impl: TvSeriesRepositoryImpl): TvSeriesRepository

        @Binds
        @Singleton
        fun bindsBrowsedRepository(impl: RecentlyBrowsedRepositoryImpl): RecentlyBrowsedRepository

        @Binds
        @Singleton
        fun bindFavouritesRepository(impl: FavouritesRepositoryImpl): FavouritesRepository

        @Binds
        @Singleton
        fun bindPersonRepository(impl: PersonRepositoryImpl): PersonRepository

        @Binds
        @Singleton
        fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

        @Binds
        @Singleton
        fun bindSeasonRepository(impl: SeasonRepositoryImpl): SeasonRepository
    }
}
