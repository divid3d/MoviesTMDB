package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SearchResult
import com.example.moviesapp.repository.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetMediaMultiSearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(
        query: String,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<SearchResult>> {
        return searchRepository.multiSearch(
            query = query,
            deviceLanguage = deviceLanguage
        )
    }
}