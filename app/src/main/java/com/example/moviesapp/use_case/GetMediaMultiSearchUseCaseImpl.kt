package com.example.moviesapp.use_case

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SearchResult
import com.example.moviesapp.repository.search.SearchRepository
import com.example.moviesapp.use_case.interfaces.GetMediaMultiSearchUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaMultiSearchUseCaseImpl @Inject constructor(
    private val searchRepository: SearchRepository
) : GetMediaMultiSearchUseCase {
    override operator fun invoke(
        query: String,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<SearchResult>> {
        return searchRepository.multiSearch(
            query = query,
            deviceLanguage = deviceLanguage
        )
    }
}