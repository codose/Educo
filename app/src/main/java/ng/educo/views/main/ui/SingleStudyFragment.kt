package ng.educo.views.main.ui

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide

import ng.educo.R
import ng.educo.databinding.FragmentSingleStudyBinding
import ng.educo.models.Educo
import ng.educo.models.User
import ng.educo.utils.Resource
import ng.educo.utils.formatDateCreated
import ng.educo.utils.longInterestToString
import ng.educo.utils.yearToString
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject

class SingleStudyFragment : BaseFragment<FragmentSingleStudyBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel by lazy {
        ViewModelProvider(this,factory)[MainViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_single_study, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arguments = SingleStudyFragmentArgs.fromBundle(arguments!!)
        val id = arguments.id
        viewModel.getSingleEduco(id!!)

        binding.backButton.setOnClickListener {
            activity!!.onBackPressed()
        }

        viewModel.studyGroupSingleData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    showShimmer()

                }

                is Resource.Success ->{
                    val educo = it.data
                    setUpData(educo)
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    hideShimmer()
                    activity!!.onBackPressed()
                }
            }
        })

        viewModel.userDetails.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    showShimmer()

                }

                is Resource.Success ->{
                    hideShimmer()
                    val user = it.data
                    setUpUser(user)
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    activity!!.onBackPressed()
                    hideShimmer()
                }
            }
        })


    }

    @SuppressLint("SetTextI18n")
    private fun setUpUser(user: User) {
        binding.textUserTitle.text = "A ${yearToString(user.level)} ${user.school} student studying ${user.dept}"
        binding.textFullName.text = "${user.firstName} ${user.lastName}"
        Glide.with(context!!)
            .load(user.imageUrl)
            .placeholder(R.drawable.ic_undraw_profile_pic)
            .into(binding.userImageView)
    }

    private fun setUpData(educo: Educo) {
        binding.educo = educo
        binding.catTextView.text = longInterestToString(educo.category)
        binding.dateCreatedTextView.text = formatDateCreated(educo.createdAt!!)
        viewModel.getUserDetails(educo.uid)
    }

    private fun hideShimmer() {
        binding.shimmerLayout.visibility = GONE
        binding.shimmerLayout.stopShimmer()
        binding.mainLayout.visibility = VISIBLE
    }

    private fun showShimmer() {
        binding.shimmerLayout.visibility = VISIBLE
        binding.shimmerLayout.startShimmer()
        binding.mainLayout.visibility = GONE

    }

    override fun getLayoutRes(): Int = R.layout.fragment_single_study
}
