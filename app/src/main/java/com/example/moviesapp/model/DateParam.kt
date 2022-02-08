package com.example.moviesapp.model

import com.example.moviesapp.other.formatted
import java.util.*

data class DateParam(private val date: Date) {
    override fun toString(): String {
        return date.formatted("yyyy-MM-dd")
    }
}