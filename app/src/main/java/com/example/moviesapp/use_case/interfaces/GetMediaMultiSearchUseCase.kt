package com.example.moviesapp.use_case.interfaces

import androidx.paging.PagingData
import com.example.moviesapp.model.DeviceLanguage
import com.example.moviesapp.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface GetMediaMultiSearchUseCase {
    operator fun invoke(
        query: String,
        deviceLanguage: DeviceLanguage
    ): Flow<PagingData<SearchResult>>
}