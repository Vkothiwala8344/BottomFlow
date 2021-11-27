package com.example.bottomflow.views.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.bottomflow.R
import com.example.bottomflow.databinding.MovieListFragmentBinding
import com.example.bottomflow.model.UiState
import com.example.bottomflow.model.TMDBMovie
import com.example.bottomflow.viewmodel.movielist.MovieListAdapter
import com.example.bottomflow.viewmodel.movielist.MovieListViewModel
import com.example.bottomflow.views.loadFragment
import com.example.bottomflow.views.moviedetail.MovieDetailFragment
import com.example.bottomflow.views.setupToolbar
import com.example.bottomflow.views.triggerSnackBar
import kotlinx.coroutines.flow.collect

class MovieListFragment : Fragment() {

    companion object {
        fun newInstance() = MovieListFragment()
    }

    private val mTag = MovieListFragment::class.java.simpleName
    private val viewModel: MovieListViewModel by activityViewModels()
    private var _binding: MovieListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieListAdapter: MovieListAdapter
    private val movieList = ArrayList<TMDBMovie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MovieListFragmentBinding.inflate(inflater, container, false)
        activity?.setTitle(R.string.movie_list)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeRefreshLayout()
        initRecyclerView()
        lifecycleScope.launchWhenStarted {
            viewModel.movieList.collect {
                when (it) {
                    is UiState.Loading -> Unit
                    is UiState.Success<*> -> {
                        Log.d(mTag, it.data.toString())
                        binding.srlMovieList.isRefreshing = false
                        movieListAdapter.updateMovies(it.data as ArrayList<TMDBMovie>)
                    }
                    is UiState.Error<*> -> {
                        binding.srlMovieList.isRefreshing = false
                        triggerSnackBar("Something went wrong")
                    }
                }
            }
        }
        viewModel.pageType.value?.let {
            setupToolbar(showBackButton = true, title = it.id)
            viewModel.fetchMovies(it)
        }
    }

    private fun initRecyclerView() {
        movieListAdapter = MovieListAdapter(movieList) { movie -> manageMovieClick(movie) }
        binding.rvMovie.let {
            it.adapter = movieListAdapter
            //it.addItemDecoration(DividerItemDecoration(it.context, LinearLayoutManager.VERTICAL))
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.srlMovieList.isRefreshing = true
        binding.srlMovieList.setOnRefreshListener {
            viewModel.pageType.value?.let {
                viewModel.fetchMovies(it)
            }
        }
    }

    private fun manageMovieClick(movie: TMDBMovie) {
        viewModel.saveMovieDetail(movie)
        loadFragment(MovieDetailFragment.newInstance())
    }
}