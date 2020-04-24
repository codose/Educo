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

import ng.educo.R
import ng.educo.databinding.FragmentChatsBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.ChatsAdapter
import ng.educo.views.main.adapters.ChatsClickListener
import ng.educo.views.main.viewmodels.RequestStudyViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ChatsFragment : BaseFragment<FragmentChatsBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

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
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChatsAdapter(context!!, ChatsClickListener {

        })
        binding.chatsProgress.indeterminateDrawable = doubleBounce
        binding.chatsRv.adapter = adapter

        viewModel.getMessages()

        viewModel.actives.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Loading ->{
                    showProgress()
                }

                is Resource.Success ->{
                    adapter.submitList(it.data)
                    hideProgress()
                }

                is Resource.Failure ->{
                    showToast(it.message)
                    hideProgress()
                }
            }
        })
    }

    private fun hideProgress() {
        binding.chatsProgress.visibility = GONE

    }

    private fun showProgress() {
        binding.chatsProgress.visibility = VISIBLE
    }

    override fun getLayoutRes(): Int = R.layout.fragment_chats

}
