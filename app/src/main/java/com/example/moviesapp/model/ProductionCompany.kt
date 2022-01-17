package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    val name: String,
    val id: Int,
    @SerializedName("logo_path")
    val logoPath: String?,
    @SerializedName("origin_country")
    val originCountry: String,
    @Transient
    val logoUrl: String?
)