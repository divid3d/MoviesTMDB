package com.example.moviesapp.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CombinedCredits(
    val cast: List<CombinedCreditsCast>,

    val crew: List<CombinedCreditsCrew>
)

@JsonClass(generateAdapter = true)
data class CombinedCreditsCast(
    override val id: Int,

    val name: String?,

    val character: String?,

    override val title: String?,

    @Json(name = "media_type")
    override val mediaType: MediaType,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Transient
    override val posterUrl: String? = null
) : CreditsPresentable {
    override val infoText: String?
        get() = character
}

@JsonClass(generateAdapter = true)
data class CombinedCreditsCrew(
    override val id: Int,

    val department: String,

    val job: String,

    override val title: String?,

    @Json(name = "media_type")
    override val mediaType: MediaType,

    @Json(name = "poster_path")
    val posterPath: String?,

    @Transient
    override val posterUrl: String? = null
) : CreditsPresentable {
    override val infoText: String?
        get() = job
}

interface CreditsPresentable {
    val id: Int
    val posterUrl: String?
    val infoText: String?
    val title: String?
    val mediaType: MediaType
}