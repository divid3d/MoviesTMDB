package com.example.moviesapp.other

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatted(format: String = "dd.MM.yyyy"): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())

    return dateFormatter.format(this)
}

fun Float.singleDecimalPlaceFormatted(): String {
    return String.format("%.1f", this)
}

fun Int.formattedMoney(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }.format(this).replace(",", " ")
}