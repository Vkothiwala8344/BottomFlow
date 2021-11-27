package com.example.bottomflow.model

import android.os.Parcelable
import com.example.bottomflow.model.TMDBMovie
import kotlinx.parcelize.Parcelize

@Parcelize
data class TMDBPage(
    var page: Int = 0,
    var results: ArrayList<TMDBMovie>? = null,
    var total_pages: Int = 0,
    var total_results: Int = 0
) : Parcelable