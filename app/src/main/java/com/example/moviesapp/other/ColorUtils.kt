package com.example.moviesapp.other

import android.graphics.Bitmap
import androidx.palette.graphics.Palette
import coil.bitmap.BitmapPool
import coil.size.Size
import coil.transform.Transformation

class IsDarkTransformation(inline val onComputed: suspend (Boolean) -> Unit = {}) : Transformation {
    override fun key() = "isDarkTransformation"

    override suspend fun transform(
        pool: BitmapPool,
        input: Bitmap,
        size: Size
    ): Bitmap {
        return input.also { bitmap ->
            onComputed(bitmap.isDark())
        }
    }
}

fun Bitmap.isDark(): Boolean {
    return Palette.from(this).generate().dominantSwatch?.run {
        getPerceptiveLuminance() < 0.5
    } ?: true
}

fun Palette.Swatch.getPerceptiveLuminance(): Float {
    val r: Int = android.graphics.Color.red(rgb)
    val g: Int = android.graphics.Color.green(rgb)
    val b: Int = android.graphics.Color.blue(rgb)


    return ((0.299f * r) + (0.587f * g) + (0.114f * b)) / 255f
}