package com.example.moviesapp.di

import com.squareup.moshi.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateJsonAdapter : JsonAdapter<Date>() {
    private val serverDateFormat = SimpleDateFormat(ServerFormat, Locale.getDefault())
    private val isoDateFormat = SimpleDateFormat(IsoFormat, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {

        val dateAsString = reader.nextString()

        if (dateAsString.isEmpty()) {
            return null
        }

        synchronized(serverDateFormat) {
            val serverFormatDate = serverDateFormat.parseOrNull(dateAsString)
            if (serverFormatDate != null) {
                return serverFormatDate
            }
        }

        synchronized(isoDateFormat) {
            val isoDate = isoDateFormat.parseOrNull(dateAsString)
            if (isoDate != null) {
                return isoDate
            }
        }

        return null
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            synchronized(serverDateFormat) {
                writer.value(value.toString())
            }
        }
    }

    private companion object {
        const val ServerFormat = "yyyy-MM-dd"
        const val IsoFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }
}

private fun SimpleDateFormat.parseOrNull(source: String): Date? {
    return try {
        parse(source)
    } catch (e: ParseException) {
        null
    }
}