package com.example.moviesapp.other

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

fun Date.formatted(format: String = "dd.MM.yyyy"): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())

    return dateFormatter.format(this)
}

fun Date.yearString() = formatted(format = "yyyy")

fun Date.timeString() = formatted(format = "HH:mm")

fun yearRangeString(from: Date?, to: Date?): String {
    return listOf(from, to)
        .mapNotNull { date -> date?.yearString() }
        .distinct()
        .joinToString(separator = " - ")
}

@OptIn(ExperimentalTime::class)
fun Int.formattedRuntime(): String? {
    return minutes.toComponents { hours, minutes, _, _ ->
        val hoursString = if (hours > 0) "${hours}h" else null
        val minutesString = if (minutes > 0) "${minutes}m" else null

        listOfNotNull(hoursString, minutesString).run {
            if (isEmpty()) null else joinToString(separator = " ")
        }
    }
}

fun Float.singleDecimalPlaceFormatted(): String {
    return String.format("%.1f", this)
}

fun Int.formattedMoney(): String {
    return NumberFormat.getCurrencyInstance(Locale.US).apply {
        maximumFractionDigits = 0
    }.format(this).replace(",", " ")
}
