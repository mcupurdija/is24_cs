package com.mcupurdija.is24_cs.networking

import com.mcupurdija.is24_cs.networking.schema.RepoSchema
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface RepoApi {

    @GET("users/google/repos")
    @Headers(
        "Accept: application/json; charset=utf-8",
        "Accept-Language: en"
    )
    suspend fun getRepos(
        @Query("per_page") perPage: Int,
        @Query("page") page: Int
    ): Response<List<RepoSchema>>
}