package com.example.moviesapp.use_case

import com.example.moviesapp.repository.search.SearchRepository
import com.example.moviesapp.use_case.interfaces.MediaSearchQueriesUseCase
import javax.inject.Inject

class MediaSearchQueriesUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository
) : MediaSearchQueriesUseCase {
    override suspend operator fun invoke(query: String) = searchRepository.searchQueries(query)
}