package ng.educo.views.main.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_single_request.*
import kotlinx.android.synthetic.main.fragment_single_study.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.databinding.DialogApplyLayoutBinding
import ng.educo.databinding.DialogDeleteConfirmationBinding
import ng.educo.databinding.FragmentSingleStudyBinding
import ng.educo.models.Educo
import ng.educo.models.Request
import ng.educo.utils.*
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SingleStudyFragment : BaseFragment<FragmentSingleStudyBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private lateinit var sender : String
    private lateinit var educo : Educo
    private lateinit var request : Request


    val viewModel by lazy {
        ViewModelProvider(this,factory)[MainViewModel::class.java]
    }
    private val dialog by lazy{
        Dialog(context!!)
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
            showAlert()
        }

        binding.trashButton.setOnClickListener {
            showDeleteAlert()
        }

        viewModel.educoDelete.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    dialog.dismiss()
                    showProgress()
                }

                is Resource.Success -> {
                    hideProgress()
                    showToast(it.data)
                    activity!!.onBackPressed()
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    hideProgress()
                }
            }
        })


        viewModel.studyGroupSingleData.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    showShimmer()
                }

                is Resource.Success ->{
                    val educo = it.data
                    sender = auth.currentUser!!.uid
                    this.educo = educo
                    setUpData()
                    hideShimmer()
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
                    dialog.dismiss()
                    showProgress()
                }

                is Resource.Success -> {
                    hideProgress()
                    showToast(it.data)
                    activity!!.onBackPressed()
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    hideProgress()
                }
            }
        })

    }

    private fun hideProgress() {
        applyProgress.visibility = GONE
        applyButton.isEnabled = true
        applyButton.visibility = VISIBLE
    }

    private fun showProgress() {
        applyProgress.visibility = VISIBLE
        applyButton.isEnabled = false
        applyButton.visibility = INVISIBLE
    }

    private fun applyForStudy(message : String) {
        request = Request(message, 0, App.appUser!!, educo)
        viewModel.sendRequest(educo.user.uid,request)
    }

    private fun deleteEduco(){
        viewModel.deleteEduco(educo.id)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpData() {
        if(educo.user.uid == App.appUser?.uid){
            binding.applyButton.visibility = GONE
            binding.trashButton.visibility = VISIBLE
        } else{
            binding.trashButton.visibility = GONE
            binding.applyButton.visibility = VISIBLE
        }
        binding.educo = educo
        binding.catTextView.text = longInterestToString(educo.category)
        binding.dateCreatedTextView.text = formatDateCreated(educo.createdAt!!)
        binding.textUserTitle.text = formatTitle(educo.user)
        binding.textFullName.text = "${educo.user.firstName} ${educo.user.lastName}"
        Glide.with(context!!)
            .load(educo.user.imageUrl)
            .placeholder(R.drawable.ic_undraw_profile_pic)
            .into(binding.userImageView)
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

    private fun showAlert(){
        val dataBinding : DialogApplyLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_apply_layout, null, false)
        dialog.setContentView(dataBinding.root)
        dataBinding.apply {
            applyMessageButton.setOnClickListener {
                if(applyMessageEditText.text!!.isNotBlank()){
                    applyForStudy(applyMessageEditText.text.toString())
                }
            }
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showDeleteAlert(){
        val dataBinding : DialogDeleteConfirmationBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_delete_confirmation, null, false)
        dialog.setContentView(dataBinding.root)
        dataBinding.apply {
            deleteButton.setOnClickListener {
                deleteEduco()
            }
            noButton.setOnClickListener {
                dialog.cancel()
            }
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }


    override fun getLayoutRes(): Int = R.layout.fragment_single_study
}
