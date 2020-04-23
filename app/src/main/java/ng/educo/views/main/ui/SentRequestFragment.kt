package ng.educo.views.main.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import ng.educo.R
import ng.educo.databinding.FragmentSentRequestBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.EducoClickListener
import ng.educo.views.main.adapters.MainAdapter
import ng.educo.views.main.adapters.RequestClickListener
import ng.educo.views.main.adapters.SentRequestAdapter
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject

class SentRequestFragment : BaseFragment<FragmentSentRequestBinding>() {

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
        binding = DataBindingUtil.inflate(inflater,getLayoutRes(), container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SentRequestAdapter(context!!, RequestClickListener {

        })

        viewModel.sent.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Success -> {
                    adapter.submitList(it.data)
                }
            }
        })
        binding.sentRecyclerView.adapter = adapter


    }

    override fun getLayoutRes(): Int = R.layout.fragment_sent_request

}
