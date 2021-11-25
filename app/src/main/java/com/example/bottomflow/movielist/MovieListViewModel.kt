package com.example.bottomflow.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomflow.datasource.MovieDataSource
import com.example.bottomflow.utility.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    private val movieDataSource: MovieDataSource = MovieDataSource()
    private val _movieList = MutableSharedFlow<UiState>()
    internal val movieList = _movieList

    internal fun fetchMovies() {
        viewModelScope.launch {
            _movieList.emit(UiState.Loading)
            try {
                val response = movieDataSource.getTMDBPage()
                if (response.isSuccessful) {
                    _movieList.emit(UiState.Success(response.body()?.results))
                } else {
                    _movieList.emit(UiState.Error(response.message()))
                    _movieList.emit(UiState.Notify("Network error"))
                }
            } catch (e: Exception) {
                _movieList.emit(UiState.Error(e))
                _movieList.emit(UiState.Notify("Network error"))
            }
        }
    }

    internal fun notify(message: String) {
        viewModelScope.launch {
            _movieList.emit(UiState.Notify(message))
        }
    }
}