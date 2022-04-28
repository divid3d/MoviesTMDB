package com.example.moviesapp.use_case

import com.example.moviesapp.model.SearchQuery
import com.example.moviesapp.repository.search.SearchRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaAddSearchQueryUseCase @Inject constructor(
    private val searchRepository: SearchRepository
){
    operator fun invoke(searchQuery: SearchQuery) = searchRepository.addSearchQuery(searchQuery)
}