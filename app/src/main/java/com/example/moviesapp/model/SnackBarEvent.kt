package com.example.moviesapp.model

import androidx.annotation.StringRes
import com.example.moviesapp.R

sealed class SnackBarEvent(@StringRes val messageStringRes: Int) {
    object NetworkDisconnected : SnackBarEvent(R.string.snack_bar_network_disconnected_label)
    object NetworkConnected : SnackBarEvent(R.string.snack_bar_network_connected_label)
}