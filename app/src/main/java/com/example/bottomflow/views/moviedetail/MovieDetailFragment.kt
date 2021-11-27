package com.example.bottomflow.views.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.bottomflow.R
import com.example.bottomflow.databinding.MovieDetailFragmentBinding
import com.example.bottomflow.model.TMDBMovie
import com.example.bottomflow.viewmodel.movielist.MovieListViewModel
import com.example.bottomflow.views.loadImageFromServer
import com.example.bottomflow.views.setupToolbar
import kotlinx.coroutines.flow.collect

class MovieDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MovieDetailFragment()
    }

    private val mTag = MovieDetailFragment::class.java.simpleName
    private val movieListViewModel: MovieListViewModel by activityViewModels()
    private var _binding: MovieDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(showBackButton = true, title = R.string.movie_detail)
        lifecycleScope.launchWhenResumed {
            movieListViewModel.movieDetail.collect {
                populateData(it)
            }
        }
    }

    private fun populateData(movie: TMDBMovie) {
        binding.ivMovie.loadImageFromServer(movie.poster_path)
        binding.tvTitle.text = movie.title
        binding.tvDescription.text = movie.overview
        binding.tvOriginalTitle.text = movie.original_title
        binding.tvLanguage.text =
            getString(R.string.language, movie.original_language)
        binding.tvRating.text =
            getString(R.string.rating, movie.vote_average.toString())
        binding.tvReleaseDate.text =
            getString(R.string.release_date, movie.release_date)
    }
}