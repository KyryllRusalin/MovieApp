package com.example.movieapp.repository

import com.example.movieapp.api.ApiServices
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class ApiRepository @Inject constructor(
    private val apiServices: ApiServices
) {
    fun getPopularMoviesList(page: Int) = apiServices.getPopularMovieList(page)

    fun getMovieDetails(id: Int) = apiServices.getMovieDetails(id)
}