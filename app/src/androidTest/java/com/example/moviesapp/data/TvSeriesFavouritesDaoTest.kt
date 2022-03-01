package com.example.moviesapp.data

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.filters.SmallTest
import com.example.moviesapp.db.FavouritesDatabase
import com.example.moviesapp.db.FavouritesTvSeriesDao
import com.example.moviesapp.model.TvSeriesFavourite
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class TvSeriesFavouritesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_favourite_database")
    lateinit var database: FavouritesDatabase

    private lateinit var favouriteTvSeriesDao: FavouritesTvSeriesDao

    @Before
    fun setup() {
        hiltRule.inject()
        favouriteTvSeriesDao = database.favouritesTvSeriesDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun likeSingleTvSeries() = runTest {
        val favouriteTvSeries = TvSeriesFavourite(
            id = 0,
            posterPath = null,
            name = "Tv series name",
            addedDate = Date()
        )

        favouriteTvSeriesDao.likeTvSeries(favouriteTvSeries)

        val ids = favouriteTvSeriesDao.favouriteTvSeriesIds().first()

        Truth.assertThat(ids).contains(favouriteTvSeries.id)
    }

    @Test
    fun unlikeTvSeries() = runTest {
        val favouriteTvSeries = TvSeriesFavourite(
            id = 0,
            posterPath = null,
            name = "Tv series name",
            addedDate = Date()
        )

        favouriteTvSeriesDao.likeTvSeries(favouriteTvSeries)

        val idsAfterLike = favouriteTvSeriesDao.favouriteTvSeriesIds().first()

        Truth.assertThat(idsAfterLike).contains(favouriteTvSeries.id)

        favouriteTvSeriesDao.unlikeTvSeries(favouriteTvSeries.id)

        val idsAfterUnlike = favouriteTvSeriesDao.favouriteTvSeriesIds().first()

        Truth.assertThat(idsAfterUnlike).isEmpty()
    }

    @Test
    fun likeMultipleTvSeries() = runTest {
        val tvSeriesCount = 10
        val favouriteTvSeries = List(tvSeriesCount) { index ->
            TvSeriesFavourite(
                id = index,
                posterPath = null,
                name = "Tv series name $index",
                addedDate = Date()
            )
        }.toTypedArray()

        favouriteTvSeriesDao.likeTvSeries(*favouriteTvSeries)

        val ids = favouriteTvSeriesDao.favouriteTvSeriesIds().first()

        Truth.assertThat(ids).containsExactlyElementsIn(ids)
    }

    @Test
    fun favouriteMoviesFactory() = runTest {
        val tvSeriesCount = 20
        val favouriteTvSeries = List(tvSeriesCount) { index ->
            TvSeriesFavourite(
                id = index,
                posterPath = null,
                name = "Tv series name $index",
                addedDate = Date()
            )
        }

        favouriteTvSeriesDao.likeTvSeries(*favouriteTvSeries.toTypedArray())

        val dataSource = favouriteTvSeriesDao.favouriteTvSeries().create() as LimitOffsetDataSource
        val items: List<TvSeriesFavourite> = dataSource.loadRange(0, tvSeriesCount)

        Truth.assertThat(items).containsExactlyElementsIn(favouriteTvSeries)
    }

    @Test
    fun favouriteTvSeriesCount() = runTest {
        val tvSeriesCount = 10
        val favouriteTvSeries = List(tvSeriesCount) { index ->
            TvSeriesFavourite(
                id = index,
                posterPath = null,
                name = "Tv series name $index",
                addedDate = Date()
            )
        }.toTypedArray()

        favouriteTvSeriesDao.likeTvSeries(*favouriteTvSeries)

        val count = favouriteTvSeriesDao.favouriteTvSeriesCount().first()

        Truth.assertThat(favouriteTvSeries.count()).isEqualTo(count)
    }

}