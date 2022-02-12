package com.example.moviesapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    private val _error: MutableSharedFlow<String?> = MutableSharedFlow(replay = 0)
    val error: StateFlow<String?> =
        _error.asSharedFlow().stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    fun onError(error: String? = null) {
        viewModelScope.launch {
            _error.emit(error)
        }
    }
}