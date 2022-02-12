package com.example.moviesapp

import androidx.lifecycle.viewModelScope
import com.example.moviesapp.model.SnackBarEvent
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.NetworkStatus
import com.example.moviesapp.other.NetworkStatusTracker
import com.example.moviesapp.repository.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkStatusTracker: NetworkStatusTracker,
    private val configRepository: ConfigRepository
) : BaseViewModel() {

    private val connectionStatus = networkStatusTracker.connectionStatus


//    private val snackBarEventChannel = Channel<SnackBarEvent?>(Channel.BUFFERED)
//    val snackBarEventFlow: StateFlow<SnackBarEvent?> = snackBarEventChannel.receiveAsFlow()
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val networkSnackBarEvent: StateFlow<SnackBarEvent?> = connectionStatus.map { status ->
        when (status) {
            NetworkStatus.Connected -> SnackBarEvent.NetworkConnected
            NetworkStatus.Disconnected -> SnackBarEvent.NetworkDisconnected
        }
    }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(10), null)

    val imageUrlParser: StateFlow<ImageUrlParser?> = configRepository.imageUrlParser
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}