package ng.educo.views.categories.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy

import ng.educo.R
import ng.educo.databinding.FragmentInterestsBinding
import ng.educo.views.base.BaseFragment
import ng.educo.views.categories.CategoryViewModel
import ng.educo.views.categories.CategoryViewModelFactory
import ng.educo.views.categories.InterestsAdapter
import ng.educo.views.categories.MyItemDetailsLookup


class InterestsFragment : BaseFragment<FragmentInterestsBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,getLayoutRes(),container,false)

        val adapter = InterestsAdapter()

        binding.fragmentsInterestsRv.adapter = adapter

        val viewModelFactory = CategoryViewModelFactory(adapter, binding.fragmentsInterestsRv)

        val viewModel = ViewModelProvider(this, viewModelFactory).get(CategoryViewModel::class.java)

        binding.lifecycleOwner = this

        adapter.tracker = viewModel.tracker

        adapter.submitList(viewModel.list.value)
        return binding.root
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_interests
    }

}



