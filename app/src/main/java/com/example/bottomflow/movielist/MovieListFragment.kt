package com.example.bottomflow.movielist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomflow.adapters.MovieListAdapter
import com.example.bottomflow.databinding.MovieListFragmentBinding
import com.example.bottomflow.home.HomeFragment.Companion.MOVIE_TYPE_BUNDLE_KEY
import com.example.bottomflow.home.HomeFragment.Companion.MOVIE_TYPE_REQUEST_KEY
import com.example.bottomflow.moviedetail.MovieDetailFragment
import com.example.bottomflow.utility.PageType
import com.example.bottomflow.utility.UiState
import com.example.bottomflow.utility.Utils.loadFragment
import com.example.bottomflow.utility.Utils.triggerSnackBar
import com.example.bottomflow.utility.interfaces.OnItemClick
import com.example.bottomflow.utility.model.TMDBMovie
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.*

class MovieListFragment : Fragment(), OnItemClick {

    companion object {
        fun newInstance() = MovieListFragment()
        const val MOVIE_BUNDLE_KEY = "movie"
        const val MOVIE_REQUEST_KEY = "movieRequestKey"
    }

    private val mTag = MovieListFragment::class.java.simpleName
    private lateinit var viewModel: MovieListViewModel
    private var _binding: MovieListFragmentBinding? = null
    private val binding get() = _binding!!
    private val movieList = ArrayList<TMDBMovie>()
    private lateinit var movieListAdapter: MovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[MovieListViewModel::class.java]

        // Use the Kotlin extension in the fragment-ktx artifact
        val fragmentManager = activity?.supportFragmentManager
        fragmentManager?.setFragmentResultListener(
            MOVIE_TYPE_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val pageType: PageType? = bundle.getParcelable(MOVIE_TYPE_BUNDLE_KEY)
            if (pageType != null) {
                viewModel.savePageType(pageType)
            }
        }
        _binding = MovieListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSwipeRefreshLayout()
        initRecyclerView()
        lifecycleScope.launchWhenStarted {
            viewModel.movieList.collect {
                when (it) {
                    is UiState.Error<*> -> {
                        Log.d(mTag, it.error.toString())
                        binding.srlMovieList.isRefreshing = false
                    }
                    is UiState.Loading -> Unit
                    is UiState.Notify<*> -> {
                        triggerSnackBar(binding.root, it.message.toString())
                    }
                    is UiState.Success<*> -> {
                        Log.d(mTag, it.data.toString())
                        binding.srlMovieList.isRefreshing = false
                        movieListAdapter.updateMovies(it.data as ArrayList<TMDBMovie>)
                    }
                }
            }
        }
        lifecycleScope.launchWhenResumed {
            viewModel.pageType.collectLatest {
                activity?.title = getString(it.id)
                viewModel.fetchMovies(it)
            }
        }
    }

    override fun <T> onClick(item: T) {
        // Use the Kotlin extension in the fragment-ktx artifact
        val fragmentManager = activity?.supportFragmentManager
        val bundle = Bundle().apply {
            this.putParcelable(MOVIE_BUNDLE_KEY, item as TMDBMovie)
        }
        fragmentManager?.setFragmentResult(MOVIE_REQUEST_KEY, bundle)
        loadFragment(fragmentManager, MovieDetailFragment.newInstance())
    }

    private fun initRecyclerView() {
        lifecycleScope.launchWhenResumed {
            viewModel.movieList.collectLatest {
                when (it) {
                    is UiState.Success<*> -> {
                        movieList.addAll(it.data as ArrayList<TMDBMovie>)
                    }
                    else -> Unit
                }
            }

        }
        binding.rvMovie.let {
            movieListAdapter = MovieListAdapter(movieList, this)
            it.adapter = movieListAdapter
            it.addItemDecoration(DividerItemDecoration(it.context, LinearLayoutManager.VERTICAL))
            //it.addItemDecoration(DividerItemDecoration(it.context, LinearLayoutManager.HORIZONTAL))
        }
    }

    private fun initSwipeRefreshLayout() {
        binding.srlMovieList.isRefreshing = true
        binding.srlMovieList.setOnRefreshListener {
            lifecycleScope.launchWhenStarted {
                viewModel.pageType.collectLatest {
                    viewModel.fetchMovies(it)
                }
            }
        }
    }
}