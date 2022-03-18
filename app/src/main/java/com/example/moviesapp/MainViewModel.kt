package com.example.moviesapp

import androidx.lifecycle.viewModelScope
import com.example.moviesapp.model.SnackBarEvent
import com.example.moviesapp.other.ImageUrlParser
import com.example.moviesapp.other.NetworkStatus
import com.example.moviesapp.other.NetworkStatusTracker
import com.example.moviesapp.repository.config.ConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkStatusTracker: NetworkStatusTracker,
    private val configRepository: ConfigRepository
) : BaseViewModel() {

    private val connectionStatus = networkStatusTracker.connectionStatus

    val networkSnackBarEvent: StateFlow<SnackBarEvent?> = connectionStatus.mapLatest { status ->
        when (status) {
            NetworkStatus.Connected -> SnackBarEvent.NetworkConnected
            NetworkStatus.Disconnected -> SnackBarEvent.NetworkDisconnected
        }
    }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val sameBottomBarRouteChannel: Channel<String> = Channel()
    val sameBottomBarRoute: Flow<String> = sameBottomBarRouteChannel.receiveAsFlow()

    val imageUrlParser: StateFlow<ImageUrlParser?> = configRepository.getImageUrlParser()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateLocale() {
        configRepository.updateLocale()
    }

    fun onSameRouteSelected(route: String) {
        viewModelScope.launch {
            sameBottomBarRouteChannel.send(route)
        }
    }
}