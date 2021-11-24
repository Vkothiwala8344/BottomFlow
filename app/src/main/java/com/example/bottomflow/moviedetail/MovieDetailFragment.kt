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
import com.example.bottomflow.utility.Movie
import com.example.bottomflow.utility.Utils.loadImageFromServer

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
        _binding = MovieDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Use the Kotlin extension in the fragment-ktx artifact
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.setFragmentResultListener(
            MOVIE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val result: Movie? = bundle.getParcelable(MOVIE_BUNDLE_KEY)
            populateData(result as Movie)
        }
    }

    private fun populateData(movie: Movie) {
        loadImageFromServer(binding.ivMovie, movie.imageUrl)
        binding.tvTitle.text = movie.name
        binding.tvDescription.text = getString(R.string.bottom_sheet_subtitle)
    }
}