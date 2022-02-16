package com.example.moviesapp.di

import com.squareup.moshi.*
import java.text.SimpleDateFormat
import java.util.*

class DateJsonAdapter : JsonAdapter<Date>() {
    private val dateFormat = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
        return try {
            val dateAsString = reader.nextString()
            synchronized(dateFormat) {
                dateFormat.parse(dateAsString)
            }
        } catch (e: Exception) {
            null
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            synchronized(dateFormat) {
                writer.value(value.toString())
            }
        }
    }

    private companion object {
        const val SERVER_FORMAT = "yyyy-MM-dd"
    }
}