package com.example.bottomflow.model

sealed class UiState {

    object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error<T>(val error: T) : UiState()
}
