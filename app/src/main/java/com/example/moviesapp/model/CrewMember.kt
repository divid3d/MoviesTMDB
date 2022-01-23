package com.example.moviesapp.model

import com.google.gson.annotations.SerializedName

data class CrewMember(
    val id: Int,
    val adult: Boolean,
    val gender: Int?,
    @SerializedName("known_for_department")
    val knownForDepartment: String?,
    val name: String,
    @SerializedName("original_name")
    val originalName: String?,
    val popularity: Float,
    @SerializedName("profile_path")
    override val profilePath: String?,
    @SerializedName("credit_id")
    val creditId: String,
    val department: String,
    val job: String,
    override val profileUrl: String?
) : Member {
    override val firstLine: String
        get() = name
    override val secondLine: String
        get() = job
}