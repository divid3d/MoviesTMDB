package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.MovieType
import com.example.moviesapp.model.Presentable
import kotlinx.coroutines.flow.Flow

interface GetMoviesOfTypeUseCase {
    operator fun invoke(
        type: MovieType,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Presentable>>
}