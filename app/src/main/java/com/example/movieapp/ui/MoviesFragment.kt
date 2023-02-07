package com.example.movieapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.adapters.LoadMoreAdapter
import com.example.movieapp.adapters.MoviesAdapter
import com.example.movieapp.databinding.FragmentMoviesBinding
import com.example.movieapp.repository.ApiRepository
import com.example.movieapp.response.MovieListResponse
import com.example.movieapp.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding

    private val viewModel : MoviesViewModel by viewModels()

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleScope.launchWhenCreated {
                viewModel.movieList.collect {
                    moviesAdapter.submitData(it)
                }
            }

            rlMovies.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = moviesAdapter
            }

            lifecycleScope.launchWhenCreated {
                moviesAdapter.loadStateFlow.collect {
                    val state = it.refresh
                    prgBarMovies.isVisible = state is LoadState.Loading
                }
            }

            rlMovies.adapter = moviesAdapter.withLoadStateFooter(
                LoadMoreAdapter {
                    moviesAdapter.retry()
                }
            )

            moviesAdapter.setOnItemClickListener {
                val directions = MoviesFragmentDirections.actionMoviesFragmentToMoviesDetailsFragment(
                    it.id
                )
                findNavController().navigate(directions)
            }
        }
    }
}