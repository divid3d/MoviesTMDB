package com.example.moviesapp.use_case

import android.graphics.Bitmap
import com.example.moviesapp.other.Roi
import com.example.moviesapp.other.TextRecognitionHelper
import com.example.moviesapp.use_case.interfaces.ScanBitmapForTextUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScanBitmapForTextUseCaseImpl @Inject constructor(
    private val textRecognitionHelper: TextRecognitionHelper
) : ScanBitmapForTextUseCase {
    override fun invoke(
        bitmap: Bitmap,
        rotation: Float,
        roi: Roi?
    ): Flow<TextRecognitionHelper.ScanState> {
        return textRecognitionHelper.scanTextFromBitmap(bitmap, rotation, roi)
    }
}