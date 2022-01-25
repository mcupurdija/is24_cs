package com.mcupurdija.is24_cs.repository

import com.mcupurdija.is24_cs.networking.RetrofitInstance

class RepoRepository {

    suspend fun getRepos(perPage: Int, page: Int) =
        RetrofitInstance.api.getRepos(perPage, page)
}