package com.example.bottomflow.repository.datasource

import android.util.Log
import com.example.bottomflow.model.UiState
import com.example.bottomflow.repository.datasource.remote.MovieService
import com.example.bottomflow.repository.datasource.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MovieDataSource {

    private val mTAG = MovieDataSource::class.java.simpleName
    private val retrofitClient = RetrofitClient.getInstance()
    private val movieService = retrofitClient.create(MovieService::class.java)

    suspend fun getTMDBTopRatedPage(): Flow<UiState> {
        return flow {
            try {
                val response = movieService.getTMDBTopRatedPage()
                val result = if (response.isSuccessful) {
                    UiState.Success(response.body()?.results)
                } else {
                    Log.d(mTAG, "getTMDBTopRatedPage response: ${response.errorBody().toString()}")
                    UiState.Error(response.errorBody())
                }
                emit(result)
            } catch (e: Exception) {
                Log.d(mTAG, "getTMDBTopRatedPage response: $e")
                emit(UiState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTMDBPopularPage(): Flow<UiState> {
        return flow {
            try {
                val response = movieService.getTMDBPopularPage()
                val result = if (response.isSuccessful) {
                    UiState.Success(response.body()?.results)
                } else {
                    Log.d(mTAG, "getTMDBPopularPage response: ${response.errorBody().toString()}")
                    UiState.Error(response.errorBody())
                }
                emit(result)
            } catch (e: Exception) {
                Log.d(mTAG, "getTMDBPopularPage response: $e")
                emit(UiState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getTMDBNowPlayingPage(): Flow<UiState> {
        return flow {
            try {
                val response = movieService.getTMDBNowPlayingPage()
                val result = if (response.isSuccessful) {
                    UiState.Success(response.body()?.results)
                } else {
                    Log.d(
                        mTAG,
                        "getTMDBNowPlayingPage response: ${response.errorBody().toString()}"
                    )
                    UiState.Error(response.errorBody())
                }
                emit(result)
            } catch (e: Exception) {
                Log.d(mTAG, "getTMDBNowPlayingPage response: $e")
                emit(UiState.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}