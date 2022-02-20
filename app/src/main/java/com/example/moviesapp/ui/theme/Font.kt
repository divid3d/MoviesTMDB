package com.example.moviesapp.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.moviesapp.R


val Lato = FontFamily(
    Font(R.font.lato_black, weight = FontWeight.Black),
    Font(R.font.lato_bold, weight = FontWeight.Bold),
    Font(R.font.lato_regular, weight = FontWeight.Normal),
    Font(R.font.lato_light, weight = FontWeight.Light),
    Font(R.font.lato_thin, weight = FontWeight.Thin),

    Font(R.font.lato_black_italic, weight = FontWeight.Black, style = FontStyle.Italic),
    Font(R.font.lato_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.lato_italic, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(R.font.lato_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
    Font(R.font.lato_thin_italic, weight = FontWeight.Thin, style = FontStyle.Italic)
)