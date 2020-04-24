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
import com.bumptech.glide.load.engine.DiskCacheStrategy

import ng.educo.R
import ng.educo.databinding.FragmentSingleRequestBinding
import ng.educo.models.Educo
import ng.educo.models.Message
import ng.educo.models.Request
import ng.educo.models.User
import ng.educo.utils.Resource
import ng.educo.utils.formatTitle
import ng.educo.utils.setOneToOneChat
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.viewmodels.RequestStudyViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SingleRequestFragment : BaseFragment<FragmentSingleRequestBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var request : Request

    private lateinit var educo : Educo

    private lateinit var user : User



    val viewModel by lazy {
        ViewModelProvider(this,factory)[RequestStudyViewModel::class.java]
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,getLayoutRes(), container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = SingleRequestFragmentArgs.fromBundle(arguments!!)
        val requestId = args.requestsId
        viewModel.getRequests(requestId)

        binding.singleRequestProgress.indeterminateDrawable = doubleBounce

        viewModel.msgSent.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading ->{
                    showProgress()
                }
                is Resource.Success -> {
                    viewModel.deleteRequest(request)
                    showToast(it.data)
                }

                is Resource.Failure -> {
                    hideProgress()
                    showToast(it.message)
                }
            }
        })

        viewModel.deleted.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading ->{
                    showProgress()
                }
                is Resource.Success -> {
                    hideProgress()
                    showToast(it.data)
                }

                is Resource.Failure -> {
                    hideProgress()
                    showToast(it.message)
                }
            }
        })


        viewModel.data.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading ->{
                    showProgress()
                }
                is Resource.Success -> {
                    hideProgress()
                    request = it.data
                    educo = request.educo
                    user = request.user
                    binding.apply {
                        educoTitleTextView.text = educo.title
                        educoMessageTxtView.text  = request.message
                        fullNameTextView.text = "${user.firstName} ${user.lastName}"
                        schoolDeptTxtxView.text = formatTitle(user)
                        Glide.with(context!!)
                            .load(user.imageUrl)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .placeholder(R.drawable.ic_undraw_profile_pic)
                            .into(binding.userImage)
                    }
                    val message = Message("Request Accepted", user.uid, getAppUser()!!.uid)

                    binding.acceptButton.setOnClickListener {
                        viewModel.sendMessage(setOneToOneChat(getAppUser()!!.uid , user.uid)!!, message, user)
                    }
                }

                is Resource.Failure -> {
                    hideProgress()
                     showToast(it.message)
                }
            }
        })
    }

    private fun hideProgress() {
        binding.singleRequestProgress.visibility = GONE
    }

    private fun showProgress() {
        binding.singleRequestProgress.visibility = VISIBLE
    }

    override fun getLayoutRes(): Int = R.layout.fragment_single_request

}
