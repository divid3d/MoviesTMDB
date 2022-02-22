package com.example.moviesapp.di

import android.annotation.SuppressLint
import com.squareup.moshi.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
class DateJsonAdapter : JsonAdapter<Date>() {
    @FromJson
    override fun fromJson(reader: JsonReader): Date? {

        val dateAsString = reader.nextString()

        if (dateAsString.isEmpty()) {
            return null
        }

        val serverFormatDate = serverDateFormat.parseOrNull(dateAsString)
        if (serverFormatDate != null) {
            return serverFormatDate
        }

        val isoDate = isoDateFormat.parseOrNull(dateAsString)
        if (isoDate != null) {
            return isoDate
        }

        return null
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            writer.value(value.toString())
        }
    }

    private companion object {
        val serverDateFormat = SimpleDateFormat("yyyy-MM-dd")
        val isoDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    }
}

private fun SimpleDateFormat.parseOrNull(source: String): Date? {
    return try {
        parse(source)
    } catch (e: Exception) {
        null
    } catch (e: ArrayIndexOutOfBoundsException) {
        null
    }
}