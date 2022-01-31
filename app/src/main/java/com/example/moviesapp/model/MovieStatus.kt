package com.example.moviesapp.model

import androidx.annotation.StringRes
import com.example.moviesapp.R
import com.squareup.moshi.Json

enum class MovieStatus(val value: String) {
    @Json(name = "Rumored")
    Rumored("Rumored"),

    @Json(name = "Planned")
    Planned("Planned"),

    @Json(name = "In Production")
    InProduction("In Production"),

    @Json(name = "Post Production")
    PostProduction("Post Production"),

    @Json(name = "Released")
    Released("Released"),

    @Json(name = "Cancelled")
    Cancelled("Cancelled");

    @StringRes
    fun getLabel(): Int = when (this) {
        Rumored -> R.string.status_rumored_label
        Planned -> R.string.status_planned_label
        InProduction -> R.string.status_in_production_label
        PostProduction -> R.string.status_post_production_label
        Released -> R.string.status_released_label
        Cancelled -> R.string.status_cancelled_label
    }
}