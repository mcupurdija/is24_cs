package com.mcupurdija.is24_cs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mcupurdija.is24_cs.networking.schema.RepoSchema
import com.mcupurdija.is24_cs.repository.RepoRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class MainViewModel(
    private val repoRepository: RepoRepository, private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _reposPaging = MutableStateFlow<PagingData<RepoSchema>>(PagingData.empty())
    val reposPaging: StateFlow<PagingData<RepoSchema>> = _reposPaging

    suspend fun getReposPaging() {
        withContext(dispatcher) {
            val response = repoRepository.getReposPaging().cachedIn(viewModelScope)
            _reposPaging.value = response.first()
        }
    }
}