package com.mcupurdija.is24_cs.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mcupurdija.is24_cs.networking.RetrofitInstance
import com.mcupurdija.is24_cs.networking.schema.RepoSchema
import com.mcupurdija.is24_cs.ui.RepoPagingSource
import com.mcupurdija.is24_cs.util.Constants
import kotlinx.coroutines.flow.Flow

class RepoRepository {

    suspend fun getReposPaging(): Flow<PagingData<RepoSchema>> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.QUERY_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                RepoPagingSource(RetrofitInstance.api)
            }
        ).flow
    }
}