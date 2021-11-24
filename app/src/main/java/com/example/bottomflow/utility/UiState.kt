package com.example.bottomflow.utility

sealed class UiState {

    object Loading : UiState()
    data class Success<T>(val data: T) : UiState()
    data class Error<T>(val error: T) : UiState()
    data class Notify<T>(val message: T) : UiState()
}
