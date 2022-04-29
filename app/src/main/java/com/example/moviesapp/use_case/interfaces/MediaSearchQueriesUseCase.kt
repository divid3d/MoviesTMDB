package com.example.moviesapp.use_case.interfaces

interface MediaSearchQueriesUseCase {
    suspend operator fun invoke(query: String): List<String>
}