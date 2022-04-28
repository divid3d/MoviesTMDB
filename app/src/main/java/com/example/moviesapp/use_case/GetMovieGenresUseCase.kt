package com.example.moviesapp.use_case

import com.example.moviesapp.repository.config.ConfigRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMovieGenresUseCase @Inject constructor(
    private val configRepository: ConfigRepository
){
    operator fun invoke()  = configRepository.getMovieGenres()
}