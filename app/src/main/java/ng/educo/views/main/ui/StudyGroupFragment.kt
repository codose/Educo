package ng.educo.views.main.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

import ng.educo.R
import ng.educo.databinding.FragmentStudyGroupBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.EducoClickListener
import ng.educo.views.main.adapters.MainAdapter
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class StudyGroupFragment : BaseFragment<FragmentStudyGroupBinding>() {

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study_group, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(activity!!, factory)[MainViewModel::class.java]

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getStudyGroupData()
        }

        hideShow()

        val adapter = MainAdapter(context!!, EducoClickListener {
            openDetails(it)
        })
        binding.studyGroupRv.adapter = adapter

        viewModel.studyGroupData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    showShimmer()
                    binding.apply {
                        nothingImage.visibility = GONE
                        nothingText.visibility = GONE
                        studyGroupRv.visibility = GONE
                    }
                }

                is Resource.Success ->{
                    hideShimmer()
                    binding.swipeRefreshLayout.isRefreshing = false
                    adapter.submitList(it.data)
                    if(it.data.isEmpty()){
                        binding.apply {
                            nothingImage.visibility = VISIBLE
                            nothingText.visibility = VISIBLE
                            studyGroupRv.visibility = GONE
                        }
                    }else{
                        binding.apply {
                            nothingImage.visibility = GONE
                            nothingText.visibility = GONE
                            studyGroupRv.visibility = VISIBLE
                        }
                    }
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    showSnackBar("Network Error")
                    hideShimmer()
                }
            }
        })
    }

    private fun openDetails(id:String) {
        findNavController().navigate(StudyGroupFragmentDirections.actionStudyGroupFragmentToSingleStudyFragment(id))
    }

    private fun hideShimmer() {
        binding.shimmerLayout.visibility = GONE
        binding.shimmerLayout.stopShimmer()
    }

    private fun showShimmer() {
        binding.shimmerLayout.visibility = VISIBLE
        binding.shimmerLayout.startShimmer()
    }


    private fun hideShow() {
        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = VISIBLE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        fab?.visibility = VISIBLE
    }

    override fun getLayoutRes(): Int = R.layout.fragment_study_group

}
