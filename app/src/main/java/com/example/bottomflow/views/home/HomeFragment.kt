package com.example.bottomflow.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomflow.R
import com.example.bottomflow.databinding.HomeFragmentBinding
import com.example.bottomflow.model.BottomSheetData
import com.example.bottomflow.model.PageType
import com.example.bottomflow.model.UiState
import com.example.bottomflow.viewmodel.home.HomeListAdapter
import com.example.bottomflow.viewmodel.home.HomeViewModel
import com.example.bottomflow.viewmodel.movielist.MovieListViewModel
import com.example.bottomflow.views.Utils.triggerTermsBottomSheet
import com.example.bottomflow.views.clearAndAddAll
import com.example.bottomflow.views.loadFragment
import com.example.bottomflow.views.movielist.MovieListFragment
import com.example.bottomflow.views.setupToolbar
import com.example.bottomflow.views.triggerSnackBar
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private val movieListViewModel: MovieListViewModel by activityViewModels()

    private val mTag = HomeFragment::class.java.simpleName
    private lateinit var buttonListAdapter: HomeListAdapter
    private val buttonList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(title = R.string.home)
        initRV()
        lifecycleScope.launchWhenStarted {
            homeViewModel.apiCall.collect {
                when (it) {
                    UiState.Loading -> {
                        binding.spinner.isVisible = true
                    }
                    is UiState.Success<*> -> {
                        binding.spinner.isVisible = false
                        triggerTermsBottomSheet(
                            requireContext(),
                            it.data as BottomSheetData
                        )?.let { dialogue ->
                            // accept and decline click listener
                            dialogue.findViewById<Button>(R.id.btn_decline)
                                ?.setOnClickListener {
                                    triggerSnackBar("Terms declined")
                                    dialogue.dismiss()
                                }
                            dialogue.findViewById<Button>(R.id.btn_accept)
                                ?.setOnClickListener {
                                    triggerSnackBar("Terms accepted")
                                    dialogue.dismiss()
                                }
                            // show dialogue
                            dialogue.show()
                        }
                    }
                    is UiState.Error<*> -> {
                        binding.spinner.isVisible = false
                        Log.d(mTag, it.error.toString())
                    }
                }
            }
        }
    }

    private fun initRV() {
        buttonListAdapter = HomeListAdapter(buttonList) { itemName -> manageItemClick(itemName) }
        buttonList.clearAndAddAll(
            arrayListOf(
                /*getString(R.string.bottom_sheet),*/
                getString(PageType.TOP_RATED.id),
                getString(PageType.POPULAR.id),
                getString(PageType.NOW_PLAYING.id)
            )
        )
        binding.rvButtons.apply {
            this.adapter = buttonListAdapter
            this.addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    LinearLayoutManager.VERTICAL
                )
            )
        }
    }

    private fun manageItemClick(itemName: String) {
        when (itemName) {
            /*getString(R.string.bottom_sheet) -> homeViewModel.fetch()*/
            getString(PageType.TOP_RATED.id) -> loadFragmentWithBundle(PageType.TOP_RATED)
            getString(PageType.POPULAR.id) -> loadFragmentWithBundle(PageType.POPULAR)
            getString(PageType.NOW_PLAYING.id) -> loadFragmentWithBundle(PageType.NOW_PLAYING)
            else -> triggerSnackBar("Click event can not be handled")
        }
    }

    private fun loadFragmentWithBundle(pageType: PageType) {
        movieListViewModel.savePageType(pageType)
        loadFragment(MovieListFragment.newInstance())
    }
}