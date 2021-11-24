package com.example.bottomflow.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bottomflow.R
import com.example.bottomflow.utility.BottomSheetData
import com.example.bottomflow.utility.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    private val _apiCall = MutableSharedFlow<UiState>()
    internal val apiCall = _apiCall

    internal fun fetch() {
        viewModelScope.launch(Dispatchers.IO) {
            _apiCall.emit(UiState.Loading)
            delay(500L)
            withContext(Dispatchers.Main) {
                _apiCall.emit(
                    UiState.Success(
                        BottomSheetData(
                            R.string.bottom_sheet_title,
                            R.string.bottom_sheet_subtitle,
                            showAccept = true,
                            showDecline = true
                        )
                    )
                )
            }
        }
    }

    internal fun notify(message: String) {
        viewModelScope.launch {
            _apiCall.emit(UiState.Notify(message))
        }
    }
}