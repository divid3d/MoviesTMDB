package com.example.moviesapp.other

import androidx.room.TypeConverter
import com.example.moviesapp.model.MovieEntityType
import com.example.moviesapp.model.TvSeriesEntityType
import java.util.*

class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class MovieEntityTypeConverters {
    @TypeConverter
    fun toMovieEntityType(value: String) = enumValueOf<MovieEntityType>(value)

    @TypeConverter
    fun fromMovieEntityType(value: MovieEntityType) = value.name
}

class TvSeriesEntityTypeConverters {
    @TypeConverter
    fun toTvSeriesEntityType(value: String) = enumValueOf<TvSeriesEntityType>(value)

    @TypeConverter
    fun fromTvSeriesEntityType(value: TvSeriesEntityType) = value.name
}