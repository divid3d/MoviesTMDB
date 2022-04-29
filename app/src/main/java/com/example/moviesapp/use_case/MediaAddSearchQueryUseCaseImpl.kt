package com.example.moviesapp.use_case

import com.example.moviesapp.model.SearchQuery
import com.example.moviesapp.repository.search.SearchRepository
import com.example.moviesapp.use_case.interfaces.MediaAddSearchQueryUseCase
import javax.inject.Inject

class MediaAddSearchQueryUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository
) : MediaAddSearchQueryUseCase {
    override operator fun invoke(searchQuery: SearchQuery) =
        searchRepository.addSearchQuery(searchQuery)
}