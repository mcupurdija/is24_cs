package com.mcupurdija.is24_cs.util

sealed class RepoCallState<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : RepoCallState<T>(data)
    class Error<T>(message: String, data: T? = null) : RepoCallState<T>(data, message)
    class Loading<T> : RepoCallState<T>()
}