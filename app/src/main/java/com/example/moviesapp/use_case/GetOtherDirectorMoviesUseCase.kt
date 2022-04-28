package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.CrewMember
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetOtherDirectorMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(
        mainDirector: CrewMember,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>> {
        return movieRepository.moviesOfDirector(
            directorId = mainDirector.id,
            deviceLanguage = deviceLanguage
        )
    }
}