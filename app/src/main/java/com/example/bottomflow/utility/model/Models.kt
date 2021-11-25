package com.example.bottomflow.utility.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TMDBMovie(
    var adult: Boolean = false,
    var backdrop_path: String? = null,
    var genre_ids: List<Int>? = null,
    var id: Int = 0,
    var original_language: String? = null,
    var original_title: String? = null,
    var overview: String? = null,
    var popularity: Double = 0.0,
    var poster_path: String = "",
    var release_date: String? = null,
    var title: String? = null,
    var video: Boolean = false,
    var vote_average: Double = 0.0,
    var vote_count: Int = 0
) : Parcelable

data class TMDBPage(
    var page: Int = 0,
    var results: ArrayList<TMDBMovie>? = null,
    var total_pages: Int = 0,
    var total_results: Int = 0
)