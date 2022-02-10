package com.example.moviesapp.model

import androidx.annotation.StringRes
import com.example.moviesapp.R
import com.squareup.moshi.Json

enum class TvSeriesStatus(val value: String) {
    @Json(name = "Returning Series")
    Returning("Returning Series"),

    @Json(name = "Planned")
    Planned("Planned"),

    @Json(name = "In Production")
    InProduction("In Production"),

    @Json(name = "Ended")
    Ended("Ended"),

    @Json(name = "Cancelled")
    Cancelled("Cancelled"),

    @Json(name = "Pilot")
    Pilot("Pilot");

    @StringRes
    fun getLabel(): Int = when (this) {
        Returning -> R.string.tv_series_status_returning_label
        Planned -> R.string.tv_series_status_planned_label
        InProduction -> R.string.tv_series_status_in_production_label
        Ended -> R.string.tv_series_status_ended_label
        Cancelled -> R.string.tv_series_status_cancelled_label
        Pilot -> R.string.tv_series_status_pilot_label
    }
}