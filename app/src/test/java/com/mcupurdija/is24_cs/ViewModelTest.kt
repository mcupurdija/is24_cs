package com.mcupurdija.is24_cs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mcupurdija.is24_cs.networking.schema.RepoSchema
import com.mcupurdija.is24_cs.repository.RepoRepository
import com.mcupurdija.is24_cs.ui.MainViewModel
import com.mcupurdija.is24_cs.util.RepoCallState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import retrofit2.Response


@ExperimentalCoroutinesApi
class ViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun testNullDataReturned() = runTest {

        val response: Response<List<RepoSchema>> = mockk(relaxed = true) {
            every { isSuccessful } returns true
            every { body() } returns null
        }
        val mockedRepository: RepoRepository = mockk(relaxed = true) {
            coEvery { getRepos(any(), any()) } returns response
        }

        val subject = MainViewModel(mockedRepository, coroutineTestRule.testDispatcher)
        assert(subject.repos.value is RepoCallState.Loading)
        subject.getRepos()
        assert(subject.repos.value is RepoCallState.Error)
    }

    @Test
    fun testUnSuccessfulCall() = runTest {

        val response: Response<List<RepoSchema>> = mockk(relaxed = true) {
            every { isSuccessful } returns false
            every { body() } returns null
        }
        val mockedRepository: RepoRepository = mockk(relaxed = true) {
            coEvery { getRepos(any(), any()) } returns response
        }

        val subject = MainViewModel(mockedRepository, coroutineTestRule.testDispatcher)
        assert(subject.repos.value is RepoCallState.Loading)
        subject.getRepos()
        assert(subject.repos.value is RepoCallState.Error)
    }

    @Test
    fun testSuccessfulCall() = runTest {

        val response: Response<List<RepoSchema>> = mockk(relaxed = true) {
            every { isSuccessful } returns true
            every { body() } returns listOf()
        }
        val mockedRepository: RepoRepository = mockk(relaxed = true) {
            coEvery { getRepos(any(), any()) } returns response
        }

        val subject = MainViewModel(mockedRepository, coroutineTestRule.testDispatcher)
        assert(subject.repos.value is RepoCallState.Loading)
        subject.getRepos()
        assert(subject.repos.value is RepoCallState.Success)
    }
}