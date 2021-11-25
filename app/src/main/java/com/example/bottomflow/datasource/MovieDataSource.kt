package com.example.bottomflow.datasource

import com.example.bottomflow.utility.PageType
import com.example.bottomflow.utility.interfaces.RetrofitService
import com.example.bottomflow.utility.model.TMDBPage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class MovieDataSource {

    suspend fun getTMDBPage(pageType: PageType): Flow<Response<TMDBPage>> {
        return flow {
            when (pageType) {
                PageType.TOP_RATED -> emit(RetrofitService.getInstance().getTMDBTopRatedPage())
                PageType.POPULAR -> emit(RetrofitService.getInstance().getTMDBPopularPage())
                PageType.NOW_PLAYING -> emit(
                    RetrofitService.getInstance().getTMDBNowPlayingPage().apply {
                        if (this.isSuccessful) this.body()?.results?.shuffle()
                    })
            }
        }
    }
}