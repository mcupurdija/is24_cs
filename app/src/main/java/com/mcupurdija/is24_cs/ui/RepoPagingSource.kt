package com.mcupurdija.is24_cs.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mcupurdija.is24_cs.networking.RepoApi
import com.mcupurdija.is24_cs.networking.schema.RepoSchema

class RepoPagingSource(private val repoApi: RepoApi) :
    PagingSource<Int, RepoSchema>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepoSchema> {
        // Start refresh at position 1 if undefined.
        val position = params.key ?: 1
        return try {
            val response = repoApi.getRepos(params.loadSize, position)
            val nextKey = if (!response.isSuccessful || response.body().isNullOrEmpty()) null else position + 1
            LoadResult.Page(
                data = response.body() as List<RepoSchema>,
                prevKey = null, // Only paging forward.
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, RepoSchema>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}