package com.example.bottomflow.repository.datasource.remote

import com.example.bottomflow.model.TMDBPage
import com.example.bottomflow.repository.Constants.TMDB_ENDPOINT_NOW_PLAYING
import com.example.bottomflow.repository.Constants.TMDB_ENDPOINT_POPULAR
import com.example.bottomflow.repository.Constants.TMDB_ENDPOINT_TOP_RATED
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {

    @GET(TMDB_ENDPOINT_TOP_RATED)
    suspend fun getTMDBTopRatedPage(): Response<TMDBPage>

    @GET(TMDB_ENDPOINT_POPULAR)
    suspend fun getTMDBPopularPage(): Response<TMDBPage>

    @GET(TMDB_ENDPOINT_NOW_PLAYING)
    suspend fun getTMDBNowPlayingPage(): Response<TMDBPage>
}