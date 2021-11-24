package com.example.bottomflow.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomflow.R
import com.example.bottomflow.utility.Movie
import com.example.bottomflow.utility.Utils.loadImageFromServer
import com.example.bottomflow.utility.interfaces.OnItemClick

class MovieListAdapter(
    private var movieList: ArrayList<Movie>,
    private val listener: OnItemClick? = null
) : RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.movieTitle.text = movie.name
        loadImageFromServer(holder.movieImage, movie.imageUrl)
        holder.itemView.setOnClickListener {
            listener?.onClick(movie)
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateMovies(movies: ArrayList<Movie>) {
        this.movieList = movies
        notifyDataSetChanged()
    }

    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val movieImage: AppCompatImageView = view.findViewById(R.id.iv_movie)
        val movieTitle: TextView = view.findViewById(R.id.tv_movie_title)
    }
}