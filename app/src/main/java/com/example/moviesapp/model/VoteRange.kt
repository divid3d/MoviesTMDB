package com.example.moviesapp.model

data class VoteRange(
    val default: ClosedFloatingPointRange<Float> = 0f..10f,
    val current: ClosedFloatingPointRange<Float> = default
)