package com.example.bottomflow.viewmodel.movielist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomflow.model.PageType
import com.example.bottomflow.model.TMDBMovie
import com.example.bottomflow.model.UiState
import com.example.bottomflow.repository.datasource.MovieDataSource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MovieListViewModel : ViewModel() {

    private val movieDataSource: MovieDataSource = MovieDataSource()

    private val _movieList = MutableSharedFlow<UiState>()
    internal val movieList = _movieList.asSharedFlow()

    private val _pageType = MutableLiveData(PageType.POPULAR)
    internal val pageType = _pageType

    private val _movieDetail = MutableStateFlow(TMDBMovie())
    internal val movieDetail = _movieDetail

    internal fun savePageType(type: PageType) {
        _pageType.value = type
    }

    internal fun fetchMovies(pageType: PageType) {
        viewModelScope.launch {
            val response: Flow<UiState> = when (pageType) {
                PageType.TOP_RATED -> movieDataSource.getTMDBTopRatedPage()
                PageType.POPULAR -> movieDataSource.getTMDBPopularPage()
                PageType.NOW_PLAYING -> movieDataSource.getTMDBNowPlayingPage()
            }
            response.collect {
                when (it) {
                    is UiState.Success<*> -> _movieList.emit(UiState.Success(it.data))
                    is UiState.Error<*> -> _movieList.emit(UiState.Error(it.error))
                    else -> Unit
                }
            }
        }
    }

    internal fun saveMovieDetail(movie: TMDBMovie) {
        viewModelScope.launch {
            _movieDetail.emit(movie)
        }
    }
}