package com.example.moviesapp.ui.screens.scanner

import android.graphics.Bitmap
import androidx.lifecycle.viewModelScope
import com.example.moviesapp.BaseViewModel
import com.example.moviesapp.other.Roi
import com.example.moviesapp.other.TextRecognitionHelper
import com.example.moviesapp.use_case.interfaces.ScanBitmapForTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ScannerViewModel @Inject constructor(
    private val scanBitmapForTextUseCase: ScanBitmapForTextUseCase
) : BaseViewModel() {

    private val scanState: MutableStateFlow<TextRecognitionHelper.ScanState> = MutableStateFlow(
        TextRecognitionHelper.ScanState.Idle
    )

    val uiState: StateFlow<ScannerScreenUiState> = scanState.map { scanState ->
        val isScanInProgress = scanState is TextRecognitionHelper.ScanState.Loading
        val scanResult = when (scanState) {
            is TextRecognitionHelper.ScanState.Success -> ScanResult.Success(scanState.text)
            is TextRecognitionHelper.ScanState.Error -> ScanResult.Error(scanState.message)
            else -> null
        }

        val validationErrorResId = when (scanState) {
            is TextRecognitionHelper.ScanState.InvalidText -> scanState.errorResId
            else -> null
        }

        ScannerScreenUiState(
            scanningInProgress = isScanInProgress,
            scanResult = scanResult,
            validationErrorResId = validationErrorResId
        )
    }.stateIn(viewModelScope, SharingStarted.Eagerly, ScannerScreenUiState.default)

    fun onBitmapCaptured(bitmap: Bitmap, rotation: Float, roi: Roi?) {
        viewModelScope.launch(Dispatchers.IO) {
            scanBitmapForTextUseCase(bitmap, rotation, roi).collect { state ->
                scanState.emit(state)
            }
        }
    }
}




