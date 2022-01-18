package com.example.moviesapp.api

import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
object Timeouts {
    val connect = Duration.seconds(10)
    val write = Duration.seconds(10)
    val read = Duration.seconds(10)
}