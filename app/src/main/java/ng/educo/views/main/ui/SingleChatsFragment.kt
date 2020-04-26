package ng.educo.views.main.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_single_chats.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.databinding.FragmentSingleChatsBinding
import ng.educo.models.Educo
import ng.educo.models.Message
import ng.educo.models.Request
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Resource
import ng.educo.utils.setOneToOneChat
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.MessageAdapter
import ng.educo.views.main.adapters.MessageClickListener
import ng.educo.views.main.viewmodels.RequestStudyViewModel
import javax.inject.Inject


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SingleChatsFragment : BaseFragment<FragmentSingleChatsBinding>() {

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
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_single_chats, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBottomNav()

        binding.messageProgress.indeterminateDrawable = doubleBounce

        val args = SingleChatsFragmentArgs.fromBundle(arguments!!)

        val docId = args.id
        val receiverUid = args.userId
        val fullName = args.fullName

        viewModel.getUserDetails(receiverUid)

        binding.fullNameTextView.text = fullName

        Log.e("DocID", docId)

        viewModel.getMessageFlow(docId)

        val mAdapter = MessageAdapter(context!!, MessageClickListener {

        })
        val mLayoutManager = LinearLayoutManager(context!!)
        mLayoutManager.stackFromEnd = true
        binding.chatsRecyclerView.apply {
            adapter = mAdapter
            layoutManager = mLayoutManager
        }



        viewModel.userDetails.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Resource.Loading ->{
                    showProgress()
                }
                is Resource.Success -> {
                    hideProgress()
                    user = it.data
                    binding.fullNameTextView.text = "${user.firstName} ${user.lastName}"
                    Glide.with(this)
                        .load(user.imageUrl)
                        .placeholder(R.drawable.ic_undraw_profile_pic)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(binding.profileImageView)
                }

                is Resource.Failure -> {
                    hideProgress()
                    showToast(it.message)
                }
            }
        })

        viewModel.messages.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Resource.Loading ->{
                    showProgress()
                }

                is Resource.Success -> {
                    hideProgress()
                    mAdapter.submitList(it.data)
                    binding.chatsRecyclerView.scrollToPosition(it.data.size - 1)

                    binding.msgSendButton.setOnClickListener {
                        if(messageEditText.text.toString().trim().isNotEmpty()){
                            val text = binding.messageEditText.text.toString()
                            val message = Message(text, receiverUid, App.appUser?.uid!!)
                            viewModel.sendMessage(docId, message, user)
                            binding.messageEditText.setText("")
                        }
                    }
                }

                is Resource.Failure -> {
                    hideProgress()
                    showToast(it.message)
                }
            }
        })



        binding.backButton.setOnClickListener {
            activity!!.onBackPressed()
        }

    }

    private fun setUpBottomNav() {
        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = GONE
    }

    private fun hideProgress() {
        binding.messageProgress.visibility = GONE
    }

    private fun showProgress() {
        binding.messageProgress.visibility = VISIBLE

    }

    override fun getLayoutRes(): Int = R.layout.fragment_single_chats

}
