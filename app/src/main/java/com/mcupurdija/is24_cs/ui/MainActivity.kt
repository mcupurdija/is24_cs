package com.mcupurdija.is24_cs.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.mcupurdija.is24_cs.adapter.RepoListAdapter
import com.mcupurdija.is24_cs.databinding.ActivityMainBinding
import com.mcupurdija.is24_cs.repository.RepoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var binding: ActivityMainBinding

    lateinit var viewModel: MainViewModel
    private val repoListAdapter = RepoListAdapter()

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
            viewModel.getReposPaging()
            repoListAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.paginationProgressBar.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launchWhenResumed {
            viewModel.reposPaging.collectLatest { pagingData ->
                repoListAdapter.submitData(pagingData)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.reposList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = repoListAdapter
        }

        repoListAdapter.setOnItemClickListener {
            RepoDetailsDialogFragment.newInstance(it)
                .show(supportFragmentManager, RepoDetailsDialogFragment.TAG)
        }
    }
}