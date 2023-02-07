package com.example.movieapp.api

import com.example.movieapp.response.MovieDetailsResponse
import com.example.movieapp.response.MovieListResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("movie/popular")
    suspend fun getPopularMovieList(
        @Query("page") page: Int
    ): Response<MovieListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int
    ): Response<MovieDetailsResponse>
}