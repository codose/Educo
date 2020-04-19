package ng.educo.views.categories.ui

import android.content.Context
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
import androidx.recyclerview.widget.RecyclerView

import ng.educo.R
import ng.educo.databinding.FragmentInterestsBinding
import ng.educo.utils.App.Companion.appUser
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.categories.*
import ng.educo.views.main.MainActivity
import javax.inject.Inject


class InterestsFragment : BaseFragment<FragmentInterestsBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var recyclerView : RecyclerView

    lateinit var viewModel : CategoryViewModel

    lateinit var tracker : SelectionTracker<Long>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as CategoryActivity).categoryComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,getLayoutRes(),container,false)


        binding.completeProgress.indeterminateDrawable = doubleBounce

        recyclerView = binding.fragmentsInterestsRv

        val adapter = InterestsAdapter()

        binding.fragmentsInterestsRv.adapter = adapter

        viewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        initTracker()

        adapter.tracker = tracker

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

    override fun onResume() {
        super.onResume()
        if(viewModel.selectedList.value!!.isNotEmpty()){
            for(i in viewModel.selectedList.value!!){
                tracker.select(i)
            }
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_interests
    }
    private fun initTracker() {
        tracker = SelectionTracker.Builder<Long>("mySelection", recyclerView,
            MyItemKeyProvider(recyclerView), MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        val ghostKey = -111L
        tracker.select(ghostKey)
        tracker.hasSelection()

        tracker.addObserver( object : SelectionTracker.SelectionObserver<Long>(){
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                viewModel.selectedList.value = tracker.selection.toMutableList()
                viewModel.selectedList.value?.remove(ghostKey)
            }
        })
    }

}



