package com.mcupurdija.is24_cs.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mcupurdija.is24_cs.adapter.RepoListAdapter
import com.mcupurdija.is24_cs.repository.RepoRepository
import com.mcupurdija.is24_cs.util.Constants.Companion.QUERY_PAGE_SIZE
import com.mcupurdija.is24_cs.util.RepoCallState
import com.mcupurdija.is24_cs.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel
    private val userListAdapter = RepoListAdapter()

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val repoRepository = RepoRepository()
        val viewModelProviderFactory = MainViewModelProviderFactory(repoRepository, Dispatchers.IO)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[MainViewModel::class.java]

        setupRecyclerView()
        lifecycleScope.launch {
            viewModel.getRepos()
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            viewModel.repos.collect { response ->
                when (response) {
                    is RepoCallState.Success -> {
                        hideProgressBar()
                        response.data?.let { data ->
                            userListAdapter.differ.submitList(ArrayList(data))
                            val totalPages = data.size / QUERY_PAGE_SIZE
                            isLastPage = viewModel.pageNumber == totalPages
                        }
                    }
                    is RepoCallState.Error -> {
                        hideProgressBar()
                        response.message?.let { message ->
                            Log.e(TAG, "An error occurred: $message")
                        }
                    }
                    is RepoCallState.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                this@MainActivity.lifecycleScope.launch {
                    viewModel.getRepos()
                }
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    private fun setupRecyclerView() {
        binding.reposList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userListAdapter
            addOnScrollListener(scrollListener)
        }

        userListAdapter.setOnItemClickListener {
            RepoDetailsDialogFragment.newInstance(it)
                .show(supportFragmentManager, RepoDetailsDialogFragment.TAG)
        }
    }
}