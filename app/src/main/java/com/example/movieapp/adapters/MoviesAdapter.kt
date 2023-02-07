package com.example.movieapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import com.example.movieapp.R
import com.example.movieapp.databinding.ItemMoviesBinding
import com.example.movieapp.response.MovieListResponse
import com.example.movieapp.utils.Constants.POSTER_BASE_URL
import javax.inject.Inject

class MoviesAdapter @Inject constructor() :
    PagingDataAdapter<MovieListResponse.Result, MoviesAdapter.ViewHolder>(differCallback) {
    private lateinit var binding: ItemMoviesBinding
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemMoviesBinding.inflate(inflater, parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.set(getItem(position)!!)
        holder.setIsRecyclable(false)
    }

    inner class ViewHolder() : RecyclerView.ViewHolder(binding.root) {
        fun set(item: MovieListResponse.Result) {
            binding.apply {
                tvMovieName.text = item.originalTitle
                tvLang.text = item.originalLanguage
                tvRate.text = item.voteAverage.toString()
                tvMovieDateRelease.text = item.releaseDate

                val moviePoster = POSTER_BASE_URL + item.posterPath
                imgMovie.load(moviePoster) {
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                    scale(Scale.FILL)
                }

                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    private var onItemClickListener : ((MovieListResponse.Result) -> Unit)? = null

    fun setOnItemClickListener(listener: (MovieListResponse.Result) -> Unit) {
        onItemClickListener = listener
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<MovieListResponse.Result>() {
            override fun areItemsTheSame(
                oldItem: MovieListResponse.Result,
                newItem: MovieListResponse.Result
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MovieListResponse.Result,
                newItem: MovieListResponse.Result
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}