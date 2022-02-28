package com.example.moviesapp.di

import android.content.Context
import com.example.moviesapp.api.TmdbApiHelper
import com.example.moviesapp.data.ConfigDataSource
import com.example.moviesapp.db.FavouritesMoviesDao
import com.example.moviesapp.db.FavouritesTvSeriesDao
import com.example.moviesapp.db.RecentlyBrowsedMoviesDao
import com.example.moviesapp.db.RecentlyBrowsedTvSeriesDao
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
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.repository.tv.TvSeriesRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
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

    @Singleton
    @Provides
    fun provideConfigRepository(configDataSource: ConfigDataSource): ConfigRepository =
        ConfigRepositoryImpl(configDataSource)

    @Singleton
    @Provides
    fun provideMovieRepository(
        dispatcher: CoroutineDispatcher,
        apiHelper: TmdbApiHelper
    ): MovieRepository = MovieRepositoryImpl(dispatcher, apiHelper)

    @Singleton
    @Provides
    fun provideTvSeriesRepository(
        dispatcher: CoroutineDispatcher,
        apiHelper: TmdbApiHelper
    ): TvSeriesRepository = TvSeriesRepositoryImpl(dispatcher, apiHelper)

    @Singleton
    @Provides
    fun provideBrowsedRepository(
        externalScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        recentlyBrowsedMoviesDao: RecentlyBrowsedMoviesDao,
        recentlyBrowseTvSeriesDao: RecentlyBrowsedTvSeriesDao
    ): RecentlyBrowsedRepository = RecentlyBrowsedRepositoryImpl(
        externalScope = externalScope,
        defaultDispatcher = dispatcher,
        recentlyBrowsedMoviesDao = recentlyBrowsedMoviesDao,
        recentlyBrowsedTvSeriesDao = recentlyBrowseTvSeriesDao
    )

    @Singleton
    @Provides
    fun provideFavouritesRepository(
        externalScope: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        favouritesMoviesDao: FavouritesMoviesDao,
        favouritesTvSeriesDao: FavouritesTvSeriesDao
    ): FavouritesRepository = FavouritesRepositoryImpl(
        externalScope = externalScope,
        defaultDispatcher = dispatcher,
        favouritesMoviesDao = favouritesMoviesDao,
        favouritesTvSeriesDao = favouritesTvSeriesDao
    )

    @Singleton
    @Provides
    fun providePersonRepository(
        apiHelper: TmdbApiHelper
    ): PersonRepository = PersonRepositoryImpl(
        apiHelper = apiHelper
    )

    @Singleton
    @Provides
    fun provideSearchRepository(
        dispatcher: CoroutineDispatcher,
        apiHelper: TmdbApiHelper
    ): SearchRepository = SearchRepositoryImpl(
        defaultDispatcher = dispatcher,
        apiHelper = apiHelper
    )
}
