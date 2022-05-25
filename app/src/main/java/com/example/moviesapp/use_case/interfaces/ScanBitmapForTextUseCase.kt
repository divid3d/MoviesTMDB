package com.example.moviesapp.use_case.interfaces

import android.graphics.Bitmap
import com.example.moviesapp.other.ROI
import com.example.moviesapp.other.TextRecognitionHelper
import kotlinx.coroutines.flow.Flow

interface ScanBitmapForTextUseCase {
    operator fun invoke(
        bitmap: Bitmap,
        rotation: Float,
        roi: ROI?
    ): Flow<TextRecognitionHelper.ScanState>
}