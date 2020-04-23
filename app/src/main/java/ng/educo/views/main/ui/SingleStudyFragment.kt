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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_single_study.*

import ng.educo.R
import ng.educo.databinding.FragmentSingleStudyBinding
import ng.educo.models.Educo
import ng.educo.models.Request
import ng.educo.models.User
import ng.educo.utils.*
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject

class SingleStudyFragment : BaseFragment<FragmentSingleStudyBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var sender : String
    private lateinit var educo : Educo
    private lateinit var request : Request


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

        binding.applyProgress.indeterminateDrawable = doubleBounce

        binding.backButton.setOnClickListener {
            activity!!.onBackPressed()
        }

        binding.applyButton.setOnClickListener {
            applyForStudy()
        }

        viewModel.studyGroupSingleData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    showShimmer()
                }

                is Resource.Success ->{
                    val educo = it.data
                    sender = auth.currentUser!!.uid
                    this.educo = educo
                    setUpData(educo)
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    hideShimmer()
                    activity!!.onBackPressed()
                }
            }
        })

        viewModel.requestSent.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    showProgress()
                }

                is Resource.Success ->{
                    hideProgress()
                    showToast(it.data)
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    hideProgress()
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

    private fun hideProgress() {
        applyProgress.visibility = INVISIBLE
        applyButton.isEnabled = true
    }

    private fun showProgress() {
        applyProgress.visibility = VISIBLE
        applyButton.isEnabled = false
    }

    private fun applyForStudy() {
        request = Request("Testing0001", 0, App.appUser!!, educo)
        viewModel.sendRequest(educo.user.uId,request)
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
        viewModel.getUserDetails(educo.user.uId)
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
