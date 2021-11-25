package com.example.bottomflow.utility.interfaces

import com.example.bottomflow.utility.model.TMDBPage
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RetrofitService {

    @GET(TMDB_ENDPOINT_TOP_RATED)
    suspend fun getTMDBTopRatedPage(): Response<TMDBPage>

    @GET(TMDB_ENDPOINT_POPULAR)
    suspend fun getTMDBPopularPage(): Response<TMDBPage>

    @GET(TMDB_ENDPOINT_NOW_PLAYING)
    suspend fun getTMDBNowPlayingPage(): Response<TMDBPage>

    companion object {
        private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
        private const val TMDB_ENDPOINT_TOP_RATED =
            "movie/top_rated?api_key=ad35eeedf999e78fd5e38d13c53f5ad8&language=en-US&page=1"
        private const val TMDB_ENDPOINT_POPULAR =
            "movie/popular?api_key=ad35eeedf999e78fd5e38d13c53f5ad8&language=en-US&page=1"
        private const val TMDB_ENDPOINT_NOW_PLAYING =
            "movie/now_playing?api_key=ad35eeedf999e78fd5e38d13c53f5ad8&language=en-US&page=1"
        const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }
    }
}