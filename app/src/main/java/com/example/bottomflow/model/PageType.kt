package com.example.bottomflow.model

import android.os.Parcelable
import com.example.bottomflow.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PageType(val id: Int) : Parcelable {
    TOP_RATED(R.string.top_rated_movie_list),
    POPULAR(R.string.popular_movie_list),
    NOW_PLAYING(R.string.now_playing_movie_list)
}