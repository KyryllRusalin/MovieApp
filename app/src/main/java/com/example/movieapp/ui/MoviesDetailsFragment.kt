package com.example.movieapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMoviesDetailsBinding
import com.example.movieapp.repository.ApiRepository
import com.example.movieapp.response.MovieDetailsResponse
import com.example.movieapp.utils.Constants
import com.example.movieapp.utils.Constants.POSTER_BASE_URL
import com.example.movieapp.viewmodels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MoviesDetailsFragment : Fragment() {
    private lateinit var binding : FragmentMoviesDetailsBinding

    private var movieId = 0
    private val args : MoviesDetailsFragmentArgs by navArgs()
    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        movieId = args.movieId
        if (movieId > 0) {
            viewModel.loadDetailsMovie(movieId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.movieId
        binding.apply {
            viewModel.detailsMovie.observe(viewLifecycleOwner) { response ->
                val moviePosterURL = POSTER_BASE_URL + response.posterPath
                imgMovie.load(moviePosterURL) {
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }
                imgMovieBack.load(moviePosterURL) {
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }

                tvMovieTitle.text = response.title
                tvMovieTagLine.text = response.tagline
                tvMovieDateRelease.text = response.releaseDate
                tvMovieRating.text = response.voteAverage.toString()
                tvMovieRuntime.text = response.runtime.toString()
                tvMovieBudget.text = response.budget.toString()
                tvMovieRevenue.text = response.revenue.toString()
                tvMovieOverview.text = response.overview
            }

            viewModel.loading.observe(viewLifecycleOwner) {
                if (it) {
                    prgBarMovies.visibility = View.VISIBLE
                } else {
                    prgBarMovies.visibility = View.INVISIBLE
                }
            }
        }
    }
}