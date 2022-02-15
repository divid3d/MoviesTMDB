package com.example.moviesapp.other

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class BottomRoundedArcShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawArcPath(size = size)
        )
    }
}

fun drawArcPath(size: Size): Path {
    val middleHeight = 0.9f * size.height

    return Path().apply {
        reset()

        lineTo(size.width, 0f)
        lineTo(size.width, middleHeight)
        cubicTo(
            x1 = size.width,
            y1 = middleHeight,
            x2 = size.width / 2,
            y2 = size.height,
            x3 = 0f,
            y3 = middleHeight
        )
        lineTo(0f, 0f)

        close()
    }
}