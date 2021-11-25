package com.example.bottomflow.moviedetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.bottomflow.R
import com.example.bottomflow.databinding.MovieDetailFragmentBinding
import com.example.bottomflow.movielist.MovieListFragment.Companion.MOVIE_BUNDLE_KEY
import com.example.bottomflow.movielist.MovieListFragment.Companion.MOVIE_REQUEST_KEY
import com.example.bottomflow.utility.Utils.loadImageFromServer
import com.example.bottomflow.utility.model.TMDBMovie

class MovieDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MovieDetailFragment()
    }

    private val mTag = MovieDetailFragment::class.java.simpleName
    private lateinit var viewModel: MovieDetailViewModel
    private var _binding: MovieDetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MovieDetailViewModel::class.java]
        activity?.title = getString(R.string.movie_detail)
        _binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.setFragmentResultListener(
            MOVIE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val result: TMDBMovie? = bundle.getParcelable(MOVIE_BUNDLE_KEY)
            populateData(result as TMDBMovie)
        }
    }

    private fun populateData(movie: TMDBMovie) {
        loadImageFromServer(binding.ivMovie, movie.poster_path)
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