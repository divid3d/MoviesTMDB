package com.example.moviesapp.api

import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

@ExperimentalTime
object Timeouts {
    val connect = 10.seconds
    val write = 10.seconds
    val read = 10.seconds
}