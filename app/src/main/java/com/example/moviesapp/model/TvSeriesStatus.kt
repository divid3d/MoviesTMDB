package com.example.moviesapp.model

import androidx.annotation.StringRes
import com.example.moviesapp.R
import com.google.gson.annotations.SerializedName

enum class TvSeriesStatus(val value: String) {
    @SerializedName("Rumored")
    Rumored("Rumored"),

    @SerializedName("Planned")
    Planned("Planned"),

    @SerializedName("In Production")
    InProduction("In Production"),

    @SerializedName("Post Production")
    PostProduction("Post Production"),

    @SerializedName("Released")
    Released("Released"),

    @SerializedName("Cancelled")
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