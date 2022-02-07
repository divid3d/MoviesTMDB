package com.example.moviesapp.model

import androidx.annotation.StringRes
import com.example.moviesapp.R

enum class SortType {
    Popularity, ReleaseDate, VoteAverage, OriginalTitle, VoteCount;

    fun toSortTypeParam(order: SortOrder) = when (this) {
        Popularity -> if (order == SortOrder.Desc) SortTypeParam.PopularityDesc else SortTypeParam.PopularityAsc
        ReleaseDate -> if (order == SortOrder.Desc) SortTypeParam.ReleaseDateDesc else SortTypeParam.ReleaseDateAsc
        VoteAverage -> if (order == SortOrder.Desc) SortTypeParam.VoteAverageDesc else SortTypeParam.VoteAverageAsc
        OriginalTitle -> if (order == SortOrder.Desc) SortTypeParam.OriginalTitleDesc else SortTypeParam.OriginalTitleAsc
        VoteCount -> if (order == SortOrder.Desc) SortTypeParam.VoteCountDesc else SortTypeParam.VoteCountAsc
    }

    @StringRes
    fun getLabel(): Int = when (this) {
        Popularity -> R.string.sort_type_popularity_label
        ReleaseDate -> R.string.sort_type_release_date_label
        VoteAverage -> R.string.sort_type_vote_average_label
        OriginalTitle -> R.string.sort_type_title_label
        VoteCount -> R.string.sort_type_vote_count_label
    }
}