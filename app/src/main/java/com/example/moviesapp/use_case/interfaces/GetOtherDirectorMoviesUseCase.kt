package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.CrewMember
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import kotlinx.coroutines.flow.Flow

interface GetOtherDirectorMoviesUseCase {
    operator fun invoke(
        mainDirector: CrewMember,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>>
}