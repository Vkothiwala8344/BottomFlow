package com.example.bottomflow.viewmodel.movielist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomflow.R
import com.example.bottomflow.model.TMDBMovie
import com.example.bottomflow.views.loadImageFromServer

class MovieListAdapter(
    var movieList: ArrayList<TMDBMovie>,
    private val onMovieItemClick: (TMDBMovie) -> Unit
) :
    RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view) { onMovieItemClick(movieList[it]) }
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount() = movieList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(movies: ArrayList<TMDBMovie>) {
        this.movieList = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(view: View, onItemViewClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {
        init {
            itemView.setOnClickListener {
                onItemViewClick(absoluteAdapterPosition)
            }
        }

        private val movieImage: AppCompatImageView = view.findViewById(R.id.iv_movie)
        private val movieTitle: TextView = view.findViewById(R.id.tv_movie_title)
        private val movieRating: TextView = view.findViewById(R.id.tv_movie_rating)
        fun bind(movie: TMDBMovie) {
            movieTitle.text = movie.title
            movieRating.text =
                itemView.context.getString(R.string.rating, movie.vote_average.toString())
            movieImage.loadImageFromServer(movie.poster_path)
        }
    }
}