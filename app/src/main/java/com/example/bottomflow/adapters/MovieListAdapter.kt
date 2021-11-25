package com.example.bottomflow.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomflow.R
import com.example.bottomflow.utility.Utils.loadImageFromServer
import com.example.bottomflow.utility.interfaces.OnItemClick
import com.example.bottomflow.utility.model.TMDBMovie

class MovieListAdapter(
    private var movieList: ArrayList<TMDBMovie>,
    private val listener: OnItemClick? = null
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.movieTitle.text = movie.title
        holder.movieRating.text =
            holder.itemView.context.getString(R.string.rating, movie.vote_average.toString())
        loadImageFromServer(holder.movieImage, movie.poster_path)
        holder.itemView.setOnClickListener {
            listener?.onClick(movie)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(movies: ArrayList<TMDBMovie>) {
        this.movieList = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieImage: AppCompatImageView = view.findViewById(R.id.iv_movie)
        val movieTitle: TextView = view.findViewById(R.id.tv_movie_title)
        val movieRating: TextView = view.findViewById(R.id.tv_movie_rating)
    }
}