package com.example.moviesapp.api

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
object Timeouts {
    val connect = Duration.seconds(60)
    val write = Duration.seconds(60)
    val read = Duration.seconds(60)
}