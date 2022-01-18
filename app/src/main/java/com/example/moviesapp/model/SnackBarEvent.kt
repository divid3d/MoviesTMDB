package com.example.moviesapp.model

sealed class SnackBarEvent(val message: String) {
    object NetworkDisconnected : SnackBarEvent("Internet connection lost")
    object NetworkConnected : SnackBarEvent("Connected to internet")
}