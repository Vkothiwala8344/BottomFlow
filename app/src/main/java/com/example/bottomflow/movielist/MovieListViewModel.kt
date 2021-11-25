package com.example.bottomflow.movielist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomflow.datasource.MovieDataSource
import com.example.bottomflow.utility.PageType
import com.example.bottomflow.utility.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    private val movieDataSource: MovieDataSource = MovieDataSource()
    private val _movieList = MutableSharedFlow<UiState>()
    internal val movieList = _movieList

    private val _pageType = MutableStateFlow(PageType.POPULAR)
    internal val pageType = _pageType

    internal fun savePageType(type: PageType) {
        viewModelScope.launch {
            _pageType.emit(type)
        }
    }

    internal fun fetchMovies(pageType: PageType) {
        viewModelScope.launch {
            try {
                movieDataSource.getTMDBPage(pageType).collect {
                    if (it.isSuccessful) {
                        _movieList.emit(UiState.Success(it.body()?.results))
                    } else {
                        _movieList.emit(UiState.Error(it.message()))
                        _movieList.emit(UiState.Notify("Network error"))
                    }
                }
            } catch (e: Exception) {
                _movieList.emit(UiState.Error(e.toString()))
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