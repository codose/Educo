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
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_received_request.*

import ng.educo.R
import ng.educo.databinding.FragmentReceivedRequestBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.RequestAdapter
import ng.educo.views.main.adapters.SentRequestAdapter
import ng.educo.views.main.adapters.RequestClickListener
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ReceivedRequestFragment : BaseFragment<FragmentReceivedRequestBinding>() {

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
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.receivedProgress.indeterminateDrawable = doubleBounce
        val adapter = RequestAdapter(context!!, RequestClickListener {
            findNavController().navigate(RequestsFragmentDirections.actionRequestsFragmentToSingleRequestFragment(it.id))
        })
        viewModel.received.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Loading -> showProgress()
                is Resource.Success -> {
                    hideProgress()
                    adapter.submitList(it.data)
                }
            }
        })
        binding.receivedRV.adapter = adapter
    }

    private fun hideProgress() {
        receivedProgress.visibility = GONE
    }

    private fun showProgress() {
        receivedProgress.visibility = VISIBLE
    }

    override fun getLayoutRes(): Int = R.layout.fragment_received_request

}
