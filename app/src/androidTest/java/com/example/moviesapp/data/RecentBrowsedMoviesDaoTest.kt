package com.example.moviesapp.data

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.moviesapp.db.RecentlyBrowsedDatabase
import com.example.moviesapp.db.RecentlyBrowsedMoviesDao
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class RecentBrowsedMoviesDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_recently_browsed_database")
    lateinit var database: RecentlyBrowsedDatabase

    private lateinit var favouritesMoviesDao: RecentlyBrowsedMoviesDao

    @Before
    fun setup() {
        hiltRule.inject()
        favouritesMoviesDao = database.recentlyBrowsedMovies()
    }

    @After
    fun teardown() {
        database.close()
    }


}