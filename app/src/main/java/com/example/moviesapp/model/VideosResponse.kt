package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class VideosResponse(
    val id: Int,
    val results: List<Video>
)

@JsonClass(generateAdapter = true)
data class Video(
    val id: String,

    val key: String,

    val site: VideoSite,

    val size: Int,

    val type: String,

    val official: Boolean,

    @Json(name = "published_at")
    val publishedAt: Date?
)

enum class VideoSite(val value: String) {
    @Json(name = "YouTube")
    YouTube("YouTube"),

    @Json(name = "Vimeo")
    Vimeo("Vimeo")
}

fun Video.getThumbnailUrl(): String {
    return when (site) {
        VideoSite.YouTube -> {
            "https://img.youtube.com/vi/${key}/hqdefault.jpg"
        }
        VideoSite.Vimeo -> {
            "https://vumbnail.com/${key}.jpg"
        }
    }
}
