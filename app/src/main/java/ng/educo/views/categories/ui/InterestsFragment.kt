package ng.educo.views.categories.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy

import ng.educo.R
import ng.educo.databinding.FragmentInterestsBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.categories.CategoryViewModel
import ng.educo.views.categories.CategoryViewModelFactory
import ng.educo.views.categories.InterestsAdapter
import ng.educo.views.categories.MyItemDetailsLookup
import ng.educo.views.main.MainActivity


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

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        adapter.tracker = viewModel.tracker

        adapter.submitList(viewModel.list.value)

        viewModel.selectedList.observe(viewLifecycleOwner, Observer {
            binding.fragmentsInterestsConfirmBtn.isEnabled = it.size > 1
        })

        viewModel.completed.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {
                    buttonNotEnabled()
                }
                is Resource.Success -> {
                    val intent = Intent(context,MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                    buttonEnabled()
                }
                is Resource.Failure -> {
                    buttonEnabled()
                    showToast("${it.message} + Please try again")
                }
            }
        })

        return binding.root
    }
    private fun buttonEnabled(){
        binding.apply {
            completeProgress.visibility = View.GONE
            fragmentsInterestsConfirmBtn.isEnabled = true
        }
    }

    private fun buttonNotEnabled(){
        binding.apply {
            completeProgress.visibility = View.VISIBLE
            fragmentsInterestsConfirmBtn.isEnabled = false
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_interests
    }

}



