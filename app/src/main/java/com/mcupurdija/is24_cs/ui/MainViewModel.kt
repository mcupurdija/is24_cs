package com.mcupurdija.is24_cs.ui

import androidx.lifecycle.ViewModel
import com.mcupurdija.is24_cs.networking.schema.RepoSchema
import com.mcupurdija.is24_cs.repository.RepoRepository
import com.mcupurdija.is24_cs.util.Constants.Companion.QUERY_PAGE_SIZE
import com.mcupurdija.is24_cs.util.RepoCallState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import retrofit2.Response

class MainViewModel(
    private val repoRepository: RepoRepository, private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _repos.value = RepoCallState.Error("Exception handled: ${throwable.localizedMessage}")
    }

    var pageNumber = 1
    private val _repos = MutableStateFlow<RepoCallState<List<RepoSchema>>>(RepoCallState.Loading())
    val repos: StateFlow<RepoCallState<List<RepoSchema>>> = _repos

    var reposResponse: MutableList<RepoSchema> = mutableListOf()

    suspend fun getRepos() {
        _repos.value = RepoCallState.Loading()
        withContext(dispatcher + exceptionHandler) {
            val response = repoRepository.getRepos(QUERY_PAGE_SIZE, pageNumber)
            _repos.value = handleReposResponse(response)
        }
    }

    private fun handleReposResponse(response: Response<List<RepoSchema>>): RepoCallState<List<RepoSchema>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                pageNumber++
                reposResponse.addAll(resultResponse)
                return RepoCallState.Success(reposResponse)
            }
        }
        return RepoCallState.Error(response.message())
    }
}