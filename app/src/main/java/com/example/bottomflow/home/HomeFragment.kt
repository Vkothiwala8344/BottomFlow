package com.example.bottomflow.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bottomflow.R
import com.example.bottomflow.adapters.ButtonListAdapter
import com.example.bottomflow.databinding.HomeFragmentBinding
import com.example.bottomflow.movielist.MovieListFragment
import com.example.bottomflow.utility.BottomSheetData
import com.example.bottomflow.utility.interfaces.OnItemClick
import com.example.bottomflow.utility.UiState
import com.example.bottomflow.utility.Utils.loadFragment
import com.example.bottomflow.utility.Utils.triggerSnackBar
import com.example.bottomflow.utility.Utils.triggerTermsBottomSheet
import kotlinx.coroutines.flow.collect

class HomeFragment : Fragment(), OnItemClick {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val items = ArrayList<String>()
    private val mTag = HomeFragment::class.java.simpleName

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRV()
        lifecycleScope.launchWhenStarted {
            viewModel.apiCall.collect {
                when (it) {
                    is UiState.Error<*> -> {
                        binding.spinner.isVisible = false
                        Log.d(mTag, it.error.toString())
                    }
                    UiState.Loading -> {
                        binding.spinner.isVisible = true
                    }
                    is UiState.Notify<*> -> {
                        triggerSnackBar(binding.root, it.message.toString())
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
                                    viewModel.notify("Terms declined")
                                    dialogue.dismiss()
                                }
                            dialogue.findViewById<Button>(R.id.btn_accept)
                                ?.setOnClickListener {
                                    viewModel.notify("Terms accepted")
                                    dialogue.dismiss()
                                }
                            // show dialogue
                            dialogue.show()
                        }
                    }
                }
            }
        }
    }

    override fun <T> onClick(item: T) {
        when (item) {
            getString(R.string.bottom_sheet) -> viewModel.fetch()
            getString(R.string.retrofit) -> {
                loadFragment(activity?.supportFragmentManager, MovieListFragment.newInstance())
            }
            else -> triggerSnackBar(binding.root, "Click event can not be handled")
        }
    }

    private fun initRV() {
        items.clear()
        items.addAll(
            listOf(
                getString(R.string.bottom_sheet),
                getString(R.string.retrofit)
            )
        )
        binding.rvButtons.let {
            it.adapter = ButtonListAdapter(items, this)
            it.addItemDecoration(DividerItemDecoration(it.context, LinearLayoutManager.VERTICAL))
        }
    }
}