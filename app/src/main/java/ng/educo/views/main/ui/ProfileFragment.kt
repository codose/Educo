package ng.educo.views.main.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import ng.educo.R
import ng.educo.databinding.FragmentProfileBinding
import ng.educo.utils.App
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.categories.CategoryViewModel
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
        (requireActivity() as MainActivity).profileComponent.inject(this)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_profile, container, false)
        viewModel = ViewModelProvider(this, factory)[ProfileViewModel::class.java]

        val profileAdapter = ProfileAdapter()

        val gridLayoutManager = GridLayoutManager(context, 4)

        viewModel.getUserData()

        binding.profileRv.apply{
            layoutManager = gridLayoutManager
            adapter = profileAdapter
        }



        viewModel.getUserProfile.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> showProgress()

                is Resource.Success -> {
                    val data = it.data
                    profileAdapter.submitList(data.interest)
                    binding.fullNameTxtView.text = data.firstName + " " + data.lastName
                    binding.titleTextView.text = data.state
                    hideProgress()
                }

                is Resource.Failure -> {
                    showToast(it.message)
                    hideProgress()
                }
            }
        })
        return binding.root
    }

    private fun showProgress() {
        binding.profileProgressBar.visibility = VISIBLE
    }

    private fun hideProgress() {
        binding.profileProgressBar.visibility = INVISIBLE
    }

    override fun getLayoutRes(): Int = R.layout.fragment_profile

}
