package com.mcupurdija.is24_cs.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mcupurdija.is24_cs.repository.RepoRepository
import kotlinx.coroutines.CoroutineDispatcher

class MainViewModelProviderFactory(
    val repoRepository: RepoRepository,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repoRepository, defaultDispatcher) as T
    }
}