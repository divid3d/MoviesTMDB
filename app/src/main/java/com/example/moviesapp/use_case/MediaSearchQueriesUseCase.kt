package com.example.moviesapp.use_case

import com.example.moviesapp.repository.search.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaSearchQueriesUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(query: String) = searchRepository.searchQueries(query)
}