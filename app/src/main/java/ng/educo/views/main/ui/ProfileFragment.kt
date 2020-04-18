package ng.educo.views.main.ui

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_profile.*

import ng.educo.R
import ng.educo.databinding.FragmentProfileBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.ProfileAdapter
import ng.educo.views.main.viewmodels.ProfileViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel : ProfileViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)
        viewModel = ViewModelProvider(activity!!, factory)[ProfileViewModel::class.java]

        val profileAdapter = ProfileAdapter()

        val gridLayoutManager = GridLayoutManager(context, 4)

        binding.profileRv.apply{
            layoutManager = gridLayoutManager
            adapter = profileAdapter
        }

        binding.apply {
            backButton.setOnClickListener { activity!!.onBackPressed() }
            editButton.setOnClickListener { findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment) }
        }

        viewModel.getUserProfile.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> showProgress()

                is Resource.Success -> {
                    val data = it.data
                    profileAdapter.submitList(data.interest)
                    binding.fullNameTxtView.text = data.firstName + " " + data.lastName
                    binding.stateTextView.text = data.state + " state"
                    hideProgress()
                }

                is Resource.Failure -> {
                    showToast(it.message)
                    showProgress()
                }
            }
        })
        return binding.root
    }

    private fun showProgress() {
        binding.mainLayout.visibility = INVISIBLE
        binding.shimmerLinearLayout.visibility = VISIBLE
        binding.shimmerLayout.visibility = VISIBLE
        binding.shimmerLayout.startShimmer()
    }

    private fun hideProgress() {
        binding.mainLayout.visibility = VISIBLE
        binding.shimmerLinearLayout.visibility = GONE
        binding.shimmerLayout.visibility = GONE
        binding.shimmerLayout.stopShimmer()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_profile

}
