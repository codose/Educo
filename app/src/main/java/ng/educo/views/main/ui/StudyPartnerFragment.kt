package ng.educo.views.main.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import ng.educo.R
import ng.educo.databinding.FragmentStudyPartnerBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.MainAdapter
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class StudyPartnerFragment : BaseFragment<FragmentStudyPartnerBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel : MainViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater,getLayoutRes(),container,false)
        viewModel = ViewModelProvider(activity!!,factory)[MainViewModel::class.java]
//        viewModel.getStudyPartnerData()
        navVisibility()
        val adapter = MainAdapter()
        binding.studyPartnerRv.adapter = adapter

        viewModel.studyPartnerData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    showShimmer()
                }

                is Resource.Success ->{
                    hideShimmer()
                    adapter.submitList(it.data)
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    hideShimmer()
                }
            }
        })

        return binding.root
    }

    private fun hideShimmer() {
        binding.shimmerLayout.visibility = GONE
        binding.shimmerLayout.stopShimmer()
    }

    private fun showShimmer() {
        binding.shimmerLayout.visibility = VISIBLE
        binding.shimmerLayout.startShimmer()
    }

    private fun navVisibility() {
        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = VISIBLE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        fab?.visibility = VISIBLE
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_study_partner
    }

}
