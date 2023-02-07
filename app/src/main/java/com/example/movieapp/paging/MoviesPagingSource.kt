package com.example.movieapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.repository.ApiRepository
import com.example.movieapp.response.MovieListResponse
import retrofit2.HttpException

class MoviesPagingSource(private val repository: ApiRepository) :
    PagingSource<Int, MovieListResponse.Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieListResponse.Result> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getPopularMoviesList(currentPage)
            val data = response.body()!!.results
            val responseData = mutableListOf<MovieListResponse.Result>()

            responseData.addAll(data)
            LoadResult.Page(
                data = responseData,
                prevKey = if(currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (httpE: HttpException) {
            LoadResult.Error(httpE)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieListResponse.Result>): Int? {
        return null
    }
}