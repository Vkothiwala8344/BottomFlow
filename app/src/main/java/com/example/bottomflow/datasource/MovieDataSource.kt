package com.example.bottomflow.datasource

import com.example.bottomflow.utility.Movie
import com.example.bottomflow.utility.interfaces.RetrofitService
import retrofit2.Response

class MovieDataSource {

    suspend fun getAllMovies(): Response<ArrayList<Movie>> {
        return RetrofitService.getInstance().getAllMovies().apply {
            this.body()?.shuffle()
        }
    }
}