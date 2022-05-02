package com.example.moviesapp.di

import com.example.moviesapp.repository.browsed.RecentlyBrowsedRepository
import com.example.moviesapp.repository.config.ConfigRepository
import com.example.moviesapp.repository.favourites.FavouritesRepository
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.repository.person.PersonRepository
import com.example.moviesapp.repository.search.SearchRepository
import com.example.moviesapp.repository.season.SeasonRepository
import com.example.moviesapp.repository.tv.TvSeriesRepository
import com.example.moviesapp.use_case.*
import com.example.moviesapp.use_case.interfaces.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAddRecentlyBrowsedMovieUseCase(
        recentlyBrowsedRepository: RecentlyBrowsedRepository
    ): AddRecentlyBrowsedMovieUseCase {
        return AddRecentlyBrowsedMovieUseCaseImpl(recentlyBrowsedRepository)
    }

    @Provides
    fun provideClearRecentlyBrowsedMoviesUseCase(
        recentlyBrowsedRepository: RecentlyBrowsedRepository
    ): ClearRecentlyBrowsedMoviesUseCase {
        return ClearRecentlyBrowsedMoviesUseCaseImpl(recentlyBrowsedRepository)
    }

    @Provides
    fun provideGetAiringTodayTvSeriesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetAiringTodayTvSeriesUseCase {
        return GetAiringTodayTvSeriesUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun provideGetAllMoviesWatchProvidersUseCase(
        configRepository: ConfigRepository
    ): GetAllMoviesWatchProvidersUseCase {
        return GetAllMoviesWatchProvidersUseCaseImpl(configRepository)
    }

    @Provides
    fun provideGetDeviceLanguageUseCase(
        configRepository: ConfigRepository
    ): GetDeviceLanguageUseCase {
        return GetDeviceLanguageUseCaseImpl(configRepository)
    }

    @Provides
    fun provideGetDiscoverAllMoviesUseCase(
        movieRepository: MovieRepository
    ): GetDiscoverAllMoviesUseCase {
        return GetDiscoverAllMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetDiscoverAllTvSeriesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetDiscoverAllTvSeriesUseCase {
        return GetDiscoverAllTvSeriesUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun provideGetDiscoverMoviesUseCase(
        movieRepository: MovieRepository
    ): GetDiscoverMoviesUseCase {
        return GetDiscoverMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetDiscoverTvSeriesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetDiscoverTvSeriesUseCase {
        return GetDiscoverTvSeriesUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun provideGetEpisodeStillsUseCase(
        seasonRepository: SeasonRepository
    ): GetEpisodeStillsUseCase {
        return GetEpisodeStillsUseCaseImpl(seasonRepository)
    }

    @Provides
    fun provideGetFavouriteMoviesIdsUseCase(
        favouritesRepository: FavouritesRepository
    ): GetFavouriteMoviesIdsUseCase {
        return GetFavouriteMoviesIdsUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideGetFavouritesMovieCountUseCase(
        favouritesRepository: FavouritesRepository
    ): GetFavouritesMovieCountUseCase {
        return GetFavouritesMovieCountUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideGetFavouritesMoviesUseCase(
        favouritesRepository: FavouritesRepository
    ): GetFavouritesMoviesUseCase {
        return GetFavouritesMoviesUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideGetFavouritesTvSeriesUseCase(
        favouritesRepository: FavouritesRepository
    ): GetFavouritesTvSeriesUseCase {
        return GetFavouritesTvSeriesUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideGetFavouritesUseCase(
        favouritesRepository: FavouritesRepository
    ): GetFavouritesUseCase {
        return GetFavouritesUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideGetFavouriteTvSeriesCountUseCase(
        favouritesRepository: FavouritesRepository
    ): GetFavouriteTvSeriesCountUseCase {
        return GetFavouriteTvSeriesCountUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideGetMediaMultiSearchUseCase(
        searchRepository: SearchRepository
    ): GetMediaMultiSearchUseCase {
        return GetMediaMultiSearchUseCaseImpl(searchRepository)
    }

    @Provides
    fun provideGetMediaTypeReviewsUseCase(
        movieRepository: MovieRepository,
        tvSeriesRepository: TvSeriesRepository
    ): GetMediaTypeReviewsUseCase {
        return GetMediaTypeReviewsUseCaseImpl(movieRepository, tvSeriesRepository)
    }

    @Provides
    fun provideMovieBackdropsUseCase(
        movieRepository: MovieRepository
    ): GetMovieBackdropsUseCase {
        return GetMovieBackdropsUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetMovieCollectionUseCase(
        movieRepository: MovieRepository
    ): GetMovieCollectionUseCase {
        return GetMovieCollectionUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetMovieCreditsUseCase(
        movieRepository: MovieRepository
    ): GetMovieCreditsUseCase {
        return GetMovieCreditsUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetMovieDetailsUseCase(
        movieRepository: MovieRepository
    ): GetMovieDetailsUseCase {
        return GetMovieDetailsUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetMovieExternalIdsUseCase(
        movieRepository: MovieRepository
    ): GetMovieExternalIdsUseCase {
        return GetMovieExternalIdsUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetMovieGenresUseCase(
        configRepository: ConfigRepository
    ): GetMovieGenresUseCase {
        return GetMovieGenresUseCaseImpl(configRepository)
    }

    @Provides
    fun provideGetMovieReviewsCountUseCase(
        movieRepository: MovieRepository
    ): GetMovieReviewsCountUseCase {
        return GetMovieReviewsCountUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetMoviesOfTypeUseCase(
        getNowPlayingMoviesUseCase: GetNowPlayingMoviesUseCase,
        getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase,
        getUpcomingMoviesUseCase: GetUpcomingMoviesUseCase,
        getFavouritesMoviesUseCaseImpl: GetFavouritesMoviesUseCase,
        getRecentlyBrowsedMoviesUseCase: GetRecentlyBrowsedMoviesUseCase,
        getTrendingMoviesUseCase: GetTrendingMoviesUseCaseImpl
    ): GetMoviesOfTypeUseCase {
        return GetMoviesOfTypeUseCaseImpl(
            getNowPlayingMoviesUseCase,
            getTopRatedMoviesUseCase,
            getUpcomingMoviesUseCase,
            getFavouritesMoviesUseCaseImpl,
            getRecentlyBrowsedMoviesUseCase,
            getTrendingMoviesUseCase
        )
    }

    @Provides
    fun provideGetMovieVideosUseCase(
        movieRepository: MovieRepository
    ): GetMovieVideosUseCase {
        return GetMovieVideosUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetMovieWatchProvidersUseCase(
        movieRepository: MovieRepository
    ): GetMovieWatchProvidersUseCase {
        return GetMovieWatchProvidersUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetNowPlayingMoviesUseCase(
        movieRepository: MovieRepository
    ): GetNowPlayingMoviesUseCase {
        return GetNowPlayingMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetOnTheAirTvSeriesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetOnTheAirTvSeriesUseCase {
        return GetOnTheAirTvSeriesUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun provideGetOtherDirectorMoviesUseCase(
        movieRepository: MovieRepository
    ): GetOtherDirectorMoviesUseCase {
        return GetOtherDirectorMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetPopularMoviesUseCase(
        movieRepository: MovieRepository
    ): GetPopularMoviesUseCase {
        return GetPopularMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetRecentlyBrowsedMoviesUseCase(
        recentlyBrowsedRepository: RecentlyBrowsedRepository
    ): GetRecentlyBrowsedMoviesUseCase {
        return GetRecentlyBrowsedMoviesUseCaseImpl(recentlyBrowsedRepository)
    }

    @Provides
    fun provideGetRecentlyBrowsedTvSeriesUseCase(
        recentlyBrowsedRepository: RecentlyBrowsedRepository
    ): GetRecentlyBrowsedTvSeriesUseCase {
        return GetRecentlyBrowsedTvSeriesUseCaseImpl(recentlyBrowsedRepository)
    }

    @Provides
    fun provideGetRelatedMoviesOfTypeUseCase(
        movieRepository: MovieRepository
    ): GetRelatedMoviesOfTypeUseCase {
        return GetRelatedMoviesOfTypeUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetRelatedTvSeriesOfTypeUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetRelatedTvSeriesOfTypeUseCase {
        return GetRelatedTvSeriesOfTypeUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun provideGetSeasonCreditsUseCase(
        seasonRepository: SeasonRepository
    ): GetSeasonCreditsUseCase {
        return GetSeasonCreditsUseCaseImpl(seasonRepository)
    }

    @Provides
    fun provideGetSeasonDetailsUseCase(
        seasonRepository: SeasonRepository
    ): GetSeasonDetailsUseCase {
        return GetSeasonDetailsUseCaseImpl(seasonRepository)
    }

    @Provides
    fun provideGetSeasonVideosUseCase(
        seasonRepository: SeasonRepository
    ): GetSeasonsVideosUseCase {
        return GetSeasonsVideosUseCaseImpl(seasonRepository)
    }

    @Provides
    fun provideGetSpeechToTextAvailableUseCase(
        configRepository: ConfigRepository
    ): GetSpeechToTextAvailableUseCase {
        return GetSpeechToTextAvailableUseCaseImpl(configRepository)
    }

    @Provides
    fun provideGetTopRatedMoviesUseCase(
        movieRepository: MovieRepository
    ): GetTopRatedMoviesUseCase {
        return GetTopRatedMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetTopRatedTvSeriesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetTopRatedTvSeriesUseCase {
        return GetTopRatedTvSeriesUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun provideGetTrendingMoviesUseCase(
        movieRepository: MovieRepository
    ): GetTrendingMoviesUseCase {
        return GetTrendingMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideGetTrendingTvSeriesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetTrendingTvSeriesUseCase {
        return GetTrendingTvSeriesUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun provideGetTvSeriesGenresUseCase(
        configRepository: ConfigRepository
    ): GetTvSeriesGenresUseCase {
        return GetTvSeriesGenresUseCaseImpl(configRepository)
    }

    @Provides
    fun provideGetTvSeriesOfTypeUseCase(
        getFavouriteTvSeriesCountUseCaseImpl: GetFavouriteTvSeriesCountUseCaseImpl,
        getOnTheAirTvSeriesUseCase: GetOnTheAirTvSeriesUseCaseImpl,
        getTopRatedTvSeriesUseCase: GetTopRatedTvSeriesUseCaseImpl,
        getAiringTodayTvSeriesUseCaseImpl: GetAiringTodayTvSeriesUseCaseImpl,
        getTrendingTvSeriesUseCase: GetTrendingTvSeriesUseCaseImpl,
        getFavouriteTvSeriesUseCase: GetFavouritesTvSeriesUseCaseImpl,
        getRecentlyBrowsedTvSeriesUseCase: GetRecentlyBrowsedTvSeriesUseCaseImpl
    ): GetTvSeriesOfTypeUseCase {
        return GetTvSeriesOfTypeUseCaseImpl(
            getFavouriteTvSeriesCountUseCaseImpl,
            getOnTheAirTvSeriesUseCase,
            getTopRatedTvSeriesUseCase,
            getAiringTodayTvSeriesUseCaseImpl,
            getTrendingTvSeriesUseCase,
            getFavouriteTvSeriesUseCase,
            getRecentlyBrowsedTvSeriesUseCase
        )
    }

    @Provides
    fun provideGetUpcomingMoviesUseCase(
        movieRepository: MovieRepository
    ): GetUpcomingMoviesUseCase {
        return GetUpcomingMoviesUseCaseImpl(movieRepository)
    }

    @Provides
    fun provideLikeMovieUseCase(
        favouritesRepository: FavouritesRepository
    ): LikeMovieUseCase {
        return LikeMovieUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideMediaAddSearchQueryUseCase(
        searchRepository: SearchRepository
    ): MediaAddSearchQueryUseCase {
        return MediaAddSearchQueryUseCaseImpl(searchRepository)
    }

    @Provides
    fun provideMediaSearchQueriesUseCase(
        searchRepository: SearchRepository
    ): MediaSearchQueriesUseCase {
        return MediaSearchQueriesUseCaseImpl(searchRepository)
    }

    @Provides
    fun provideUnlikeMovieUseCase(
        favouritesRepository: FavouritesRepository
    ): UnlikeMovieUseCase {
        return UnlikeMovieUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun provideGetAllTvSeriesWatchProvidersUseCase(
        configRepository: ConfigRepository
    ): GetAllTvSeriesWatchProvidersUseCase {
        return GetAllTvSeriesWatchProvidersUseCaseImpl(configRepository)
    }

    @Provides
    fun providesGetPersonDetailsUseCase(
        personRepository: PersonRepository
    ): GetPersonDetailsUseCase {
        return GetPersonDetailsUseCaseImpl(personRepository)
    }

    @Provides
    fun providesGetCombinedCreditsUseCase(
        personRepository: PersonRepository
    ): GetCombinedCreditsUseCase {
        return GetCombinedCreditsUseCaseImpl(personRepository)
    }

    @Provides
    fun providesGetPersonExternalIdsUseCase(
        personRepository: PersonRepository
    ): GetPersonExternalIdsUseCase {
        return GetPersonExternalIdsUseCaseImpl(personRepository)
    }

    @Provides
    fun providesGetTvSeriesDetailsUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetTvSeriesDetailsUseCase {
        return GetTvSeriesDetailsUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun providesGetNextEpisodeDaysRemainingUseCase(): GetNextEpisodeDaysRemainingUseCase {
        return GetNextEpisodeDaysRemainingUseCaseImpl()
    }

    @Provides
    fun providesGetTvSeriesExternalIdsUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetTvSeriesExternalIdsUseCase {
        return GetTvSeriesExternalIdsUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun providesGetTvSeriesImagesUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetTvSeriesImagesUseCase {
        return GetTvSeriesImagesUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun providesGetTvSeriesReviewsCountUseCase(
        tvSeriesRepository: TvSeriesRepository,
    ): GetTvSeriesReviewsCountUseCase {
        return GetTvSeriesReviewsCountUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun providesGetTvSeriesVideosUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetTvSeriesVideosUseCase {
        return GetTvSeriesVideosUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun providesGetTvSeriesWatchProvidersUseCase(
        tvSeriesRepository: TvSeriesRepository
    ): GetTvSeriesWatchProvidersUseCase {
        return GetTvSeriesWatchProvidersUseCaseImpl(tvSeriesRepository)
    }

    @Provides
    fun providesAddRecentlyBrowsedTvSeriesUseCase(
        recentlyBrowsedRepository: RecentlyBrowsedRepository
    ): AddRecentlyBrowsedTvSeriesUseCase {
        return AddRecentlyBrowsedTvSeriesUseCaseImpl(recentlyBrowsedRepository)
    }

    @Provides
    fun providesGetFavouritesTvSeriesIdsUseCase(
        favouritesRepository: FavouritesRepository
    ): GetFavouriteTvSeriesIdsUseCase {
        return GetFavouriteTvSeriesIdsUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun providesLikeTvSeriesUseCase(
        favouritesRepository: FavouritesRepository
    ): LikeTvSeriesUseCase {
        return LikeTvSeriesUseCaseImpl(favouritesRepository)
    }

    @Provides
    fun providesUnlikeTvSeriesUseCase(
        favouritesRepository: FavouritesRepository
    ): UnlikeTvSeriesUseCase {
        return UnlikeTvSeriesUseCaseImpl(favouritesRepository)
    }
}