package com.example.movieapp.repository

import com.example.movieapp.api.ApiServices
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val apiServices: ApiServices
) {
    suspend fun getPopularMoviesList(page: Int) = apiServices.getPopularMovieList(page)

    suspend fun getMovieDetails(id: Int) = apiServices.getMovieDetails(id)
}