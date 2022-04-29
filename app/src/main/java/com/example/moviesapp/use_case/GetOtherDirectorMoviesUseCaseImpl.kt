package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.CrewMember
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.Movie
import com.example.moviesapp.repository.movie.MovieRepository
import com.example.moviesapp.use_case.interfaces.GetOtherDirectorMoviesUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOtherDirectorMoviesUseCaseImpl @Inject constructor(
    private val movieRepository: MovieRepository
) : GetOtherDirectorMoviesUseCase {
    override operator fun invoke(
        mainDirector: CrewMember,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<Movie>> {
        return movieRepository.moviesOfDirector(
            directorId = mainDirector.id,
            deviceLanguage = deviceLanguage
        )
    }
}