package com.example.moviesapp.use_case.interfaces

import com.example.moviesapp.model.SearchQuery

interface MediaAddSearchQueryUseCase {
    operator fun invoke(searchQuery: SearchQuery)
}