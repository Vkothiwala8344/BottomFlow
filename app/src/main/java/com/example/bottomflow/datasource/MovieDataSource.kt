package com.example.bottomflow.datasource

import com.example.bottomflow.utility.interfaces.RetrofitService
import com.example.bottomflow.utility.model.TMDBPage
import retrofit2.Response

class MovieDataSource {

    suspend fun getTMDBPage(): Response<TMDBPage> {
        return RetrofitService.getInstance().getTMDBPage()
    }
}