package com.example.moviesapp.di

import com.example.moviesapp.use_case.*
import com.example.moviesapp.use_case.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseBinds {
    @Binds
    fun provideAddRecentlyBrowsedMovieUseCase(
        impl: AddRecentlyBrowsedMovieUseCaseImpl
    ): AddRecentlyBrowsedMovieUseCase

    @Binds
    fun provideClearRecentlyBrowsedMoviesUseCase(
        impl: ClearRecentlyBrowsedMoviesUseCaseImpl
    ): ClearRecentlyBrowsedMoviesUseCase

    @Binds
    fun provideGetAiringTodayTvSeriesUseCase(
        impl: GetAiringTodayTvSeriesUseCaseImpl
    ): GetAiringTodayTvSeriesUseCase

    @Binds
    fun provideGetAllMoviesWatchProvidersUseCase(
        impl: GetAllMoviesWatchProvidersUseCaseImpl
    ): GetAllMoviesWatchProvidersUseCase

    @Binds
    fun provideGetDeviceLanguageUseCase(
        impl: GetDeviceLanguageUseCaseImpl
    ): GetDeviceLanguageUseCase

    @Binds
    fun provideGetDiscoverAllMoviesUseCase(
        impl: GetDiscoverAllMoviesUseCaseImpl
    ): GetDiscoverAllMoviesUseCase

    @Binds
    fun provideGetDiscoverAllTvSeriesUseCase(
        impl: GetDiscoverAllTvSeriesUseCaseImpl
    ): GetDiscoverAllTvSeriesUseCase

    @Binds
    fun provideGetDiscoverMoviesUseCase(
        impl: GetDiscoverMoviesUseCaseImpl
    ): GetDiscoverMoviesUseCase

    @Binds
    fun provideGetDiscoverTvSeriesUseCase(
        impl: GetDiscoverTvSeriesUseCaseImpl
    ): GetDiscoverTvSeriesUseCase

    @Binds
    fun provideGetEpisodeStillsUseCase(
        impl: GetEpisodeStillsUseCaseImpl
    ): GetEpisodeStillsUseCase

    @Binds
    fun provideGetFavouriteMoviesIdsUseCase(
        impl: GetFavouriteMoviesIdsUseCaseImpl
    ): GetFavouriteMoviesIdsUseCase

    @Binds
    fun provideGetFavouritesMovieCountUseCase(
        impl: GetFavouritesMovieCountUseCaseImpl
    ): GetFavouritesMovieCountUseCase

    @Binds
    fun provideGetFavouritesMoviesUseCase(
        impl: GetFavouritesMoviesUseCaseImpl
    ): GetFavouritesMoviesUseCase

    @Binds
    fun provideGetFavouritesTvSeriesUseCase(
        impl: GetFavouritesTvSeriesUseCaseImpl
    ): GetFavouritesTvSeriesUseCase

    @Binds
    fun provideGetFavouritesUseCase(
        impl: GetFavouritesUseCaseImpl
    ): GetFavouritesUseCase

    @Binds
    fun provideGetFavouriteTvSeriesCountUseCase(
        impl: GetFavouriteTvSeriesCountUseCaseImpl
    ): GetFavouriteTvSeriesCountUseCase

    @Binds
    fun provideGetMediaMultiSearchUseCase(
        impl: GetMediaMultiSearchUseCaseImpl
    ): GetMediaMultiSearchUseCase

    @Binds
    fun provideGetMediaTypeReviewsUseCase(
        impl: GetMediaTypeReviewsUseCaseImpl
    ): GetMediaTypeReviewsUseCase

    @Binds
    fun provideMovieBackdropsUseCase(
        impl: GetMovieBackdropsUseCaseImpl
    ): GetMovieBackdropsUseCase

    @Binds
    fun provideGetMovieCollectionUseCase(
        impl: GetMovieCollectionUseCaseImpl
    ): GetMovieCollectionUseCase

    @Binds
    fun provideGetMovieCreditsUseCase(
        impl: GetMovieCreditsUseCaseImpl
    ): GetMovieCreditsUseCase

    @Binds
    fun provideGetMovieDetailsUseCase(
        impl: GetMovieDetailsUseCaseImpl
    ): GetMovieDetailsUseCase

    @Binds
    fun provideGetMovieExternalIdsUseCase(
        impl: GetMovieExternalIdsUseCaseImpl
    ): GetMovieExternalIdsUseCase

    @Binds
    fun provideGetMovieGenresUseCase(
        impl: GetMovieGenresUseCaseImpl
    ): GetMovieGenresUseCase

    @Binds
    fun provideGetMovieReviewsCountUseCase(
        impl: GetMovieReviewsCountUseCaseImpl
    ): GetMovieReviewsCountUseCase

    @Binds
    fun provideGetMoviesOfTypeUseCase(
        impl: GetMoviesOfTypeUseCaseImpl
    ): GetMoviesOfTypeUseCase

    @Binds
    fun provideGetMovieVideosUseCase(
        impl: GetMovieVideosUseCaseImpl
    ): GetMovieVideosUseCase

    @Binds
    fun provideGetMovieWatchProvidersUseCase(
        impl: GetMovieWatchProvidersUseCaseImpl
    ): GetMovieWatchProvidersUseCase

    @Binds
    fun provideGetNowPlayingMoviesUseCase(
        impl: GetNowPlayingMoviesUseCaseImpl
    ): GetNowPlayingMoviesUseCase

    @Binds
    fun provideGetOnTheAirTvSeriesUseCase(
        impl: GetOnTheAirTvSeriesUseCaseImpl
    ): GetOnTheAirTvSeriesUseCase

    @Binds
    fun provideGetOtherDirectorMoviesUseCase(
        impl: GetOtherDirectorMoviesUseCaseImpl
    ): GetOtherDirectorMoviesUseCase

    @Binds
    fun provideGetPopularMoviesUseCase(
        impl: GetPopularMoviesUseCaseImpl
    ): GetPopularMoviesUseCase

    @Binds
    fun provideGetRecentlyBrowsedMoviesUseCase(
        impl: GetRecentlyBrowsedMoviesUseCaseImpl
    ): GetRecentlyBrowsedMoviesUseCase

    @Binds
    fun provideGetRecentlyBrowsedTvSeriesUseCase(
        impl: GetRecentlyBrowsedTvSeriesUseCaseImpl
    ): GetRecentlyBrowsedTvSeriesUseCase

    @Binds
    fun provideGetRelatedMoviesOfTypeUseCase(
        impl: GetRelatedMoviesOfTypeUseCaseImpl
    ): GetRelatedMoviesOfTypeUseCase

    @Binds
    fun provideGetRelatedTvSeriesOfTypeUseCase(
        impl: GetRelatedTvSeriesOfTypeUseCaseImpl
    ): GetRelatedTvSeriesOfTypeUseCase

    @Binds
    fun provideGetSeasonCreditsUseCase(
        impl: GetSeasonCreditsUseCaseImpl
    ): GetSeasonCreditsUseCase

    @Binds
    fun provideGetSeasonDetailsUseCase(
        impl: GetSeasonDetailsUseCaseImpl
    ): GetSeasonDetailsUseCase

    @Binds
    fun provideGetSeasonVideosUseCase(
        impl: GetSeasonsVideosUseCaseImpl
    ): GetSeasonsVideosUseCase

    @Binds
    fun provideGetSpeechToTextAvailableUseCase(
        impl: GetSpeechToTextAvailableUseCaseImpl
    ): GetSpeechToTextAvailableUseCase

    @Binds
    fun provideCameraAvailableUseCase(
        impl: GetCameraAvailableUseCaseImpl
    ): GetCameraAvailableUseCase

    @Binds
    fun provideGetTopRatedMoviesUseCase(
        impl: GetTopRatedMoviesUseCaseImpl
    ): GetTopRatedMoviesUseCase

    @Binds
    fun provideGetTopRatedTvSeriesUseCase(
        impl: GetTopRatedTvSeriesUseCaseImpl
    ): GetTopRatedTvSeriesUseCase

    @Binds
    fun provideGetTrendingMoviesUseCase(
        impl: GetTrendingMoviesUseCaseImpl
    ): GetTrendingMoviesUseCase

    @Binds
    fun provideGetTrendingTvSeriesUseCase(
        impl: GetTrendingTvSeriesUseCaseImpl
    ): GetTrendingTvSeriesUseCase

    @Binds
    fun provideGetTvSeriesGenresUseCase(
        impl: GetTvSeriesGenresUseCaseImpl
    ): GetTvSeriesGenresUseCase

    @Binds
    fun provideGetTvSeriesOfTypeUseCase(
        impl: GetTvSeriesOfTypeUseCaseImpl
    ): GetTvSeriesOfTypeUseCase

    @Binds
    fun provideGetUpcomingMoviesUseCase(
        impl: GetUpcomingMoviesUseCaseImpl
    ): GetUpcomingMoviesUseCase

    @Binds
    fun provideLikeMovieUseCase(
        impl: LikeMovieUseCaseImpl
    ): LikeMovieUseCase

    @Binds
    fun provideMediaAddSearchQueryUseCase(
        impl: MediaAddSearchQueryUseCaseImpl
    ): MediaAddSearchQueryUseCase

    @Binds
    fun provideMediaSearchQueriesUseCase(
        impl: MediaSearchQueriesUseCaseImpl
    ): MediaSearchQueriesUseCase

    @Binds
    fun provideUnlikeMovieUseCase(
        impl: UnlikeMovieUseCaseImpl
    ): UnlikeMovieUseCase

    @Binds
    fun provideGetAllTvSeriesWatchProvidersUseCase(
        impl: GetAllTvSeriesWatchProvidersUseCaseImpl
    ): GetAllTvSeriesWatchProvidersUseCase

    @Binds
    fun providesGetPersonDetailsUseCase(
        impl: GetPersonDetailsUseCaseImpl
    ): GetPersonDetailsUseCase

    @Binds
    fun providesGetCombinedCreditsUseCase(
        impl: GetCombinedCreditsUseCaseImpl
    ): GetCombinedCreditsUseCase

    @Binds
    fun providesGetPersonExternalIdsUseCase(
        impl: GetPersonExternalIdsUseCaseImpl
    ): GetPersonExternalIdsUseCase

    @Binds
    fun providesGetTvSeriesDetailsUseCase(
        impl: GetTvSeriesDetailsUseCaseImpl
    ): GetTvSeriesDetailsUseCase

    @Binds
    fun providesGetNextEpisodeDaysRemainingUseCase(
        impl: GetNextEpisodeDaysRemainingUseCaseImpl
    ): GetNextEpisodeDaysRemainingUseCase

    @Binds
    fun providesGetTvSeriesExternalIdsUseCase(
        impl: GetTvSeriesExternalIdsUseCaseImpl
    ): GetTvSeriesExternalIdsUseCase

    @Binds
    fun providesGetTvSeriesImagesUseCase(
        impl: GetTvSeriesImagesUseCaseImpl
    ): GetTvSeriesImagesUseCase

    @Binds
    fun providesGetTvSeriesReviewsCountUseCase(
        impl: GetTvSeriesReviewsCountUseCaseImpl,
    ): GetTvSeriesReviewsCountUseCase

    @Binds
    fun providesGetTvSeriesVideosUseCase(
        impl: GetTvSeriesVideosUseCaseImpl
    ): GetTvSeriesVideosUseCase

    @Binds
    fun providesGetTvSeriesWatchProvidersUseCase(
        impl: GetTvSeriesWatchProvidersUseCaseImpl
    ): GetTvSeriesWatchProvidersUseCase

    @Binds
    fun providesAddRecentlyBrowsedTvSeriesUseCase(
        impl: AddRecentlyBrowsedTvSeriesUseCaseImpl
    ): AddRecentlyBrowsedTvSeriesUseCase

    @Binds
    fun providesGetFavouritesTvSeriesIdsUseCase(
        impl: GetFavouriteTvSeriesIdsUseCaseImpl
    ): GetFavouriteTvSeriesIdsUseCase

    @Binds
    fun providesLikeTvSeriesUseCase(
        impl: LikeTvSeriesUseCaseImpl
    ): LikeTvSeriesUseCase

    @Binds
    fun providesUnlikeTvSeriesUseCase(
        impl: UnlikeTvSeriesUseCaseImpl
    ): UnlikeTvSeriesUseCase

    @Binds
    fun providesScanBitmapForTextUseCase(
        impl: ScanBitmapForTextUseCaseImpl
    ): ScanBitmapForTextUseCase
}