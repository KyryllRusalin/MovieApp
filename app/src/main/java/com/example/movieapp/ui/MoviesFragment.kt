package com.example.movieapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movieapp.adapters.MoviesAdapter
import com.example.movieapp.databinding.FragmentMoviesBinding
import com.example.movieapp.repository.ApiRepository
import com.example.movieapp.response.MovieListResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {
    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var apiRepository: ApiRepository

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
            prgBarMovies.visibility = View.VISIBLE
            apiRepository.getPopularMoviesList(1).enqueue(object : Callback<MovieListResponse> {
                override fun onResponse(
                    call: Call<MovieListResponse>,
                    response: Response<MovieListResponse>
                ) {
                    when(response.code()) {
                        200 -> {
                            prgBarMovies.visibility = View.GONE

                            response.body().let { itBody ->
                                if(itBody?.results!!.isNotEmpty()) {
                                    moviesAdapter.differ.submitList(itBody.results)
                                }

                                rlMovies.apply {
                                    layoutManager = LinearLayoutManager(requireContext())
                                    adapter = moviesAdapter
                                }

                                moviesAdapter.setOnItemClickListener {
                                    val direction = MoviesFragmentDirections.actionMoviesFragmentToMoviesDetailsFragment(
                                        it.id
                                    )
                                    findNavController().navigate(direction)
                                }
                            }
                        }
                        404 -> {
                            Toast.makeText(requireContext(), "The resource you requested could not be found.",
                                Toast.LENGTH_SHORT).show()
                        }
                        401 -> {
                            Toast.makeText(requireContext(), "Invalid API key: You must be granted a valid key.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<MovieListResponse>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    Toast.makeText(requireContext(), "Failure",
                        Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}