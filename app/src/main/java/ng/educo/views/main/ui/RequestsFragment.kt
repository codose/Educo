package ng.educo.views.main.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.badge.BadgeDrawable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.databinding.FragmentRequestsBinding
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.adapters.RequestPagerAdapter
import ng.educo.views.main.viewmodels.MainViewModel
import javax.inject.Inject


@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class RequestsFragment : BaseFragment<FragmentRequestsBinding>() {

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
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = RequestPagerAdapter(childFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        val badge: BadgeDrawable = binding.tabs.getTabAt(0)!!.orCreateBadge
        val badge2: BadgeDrawable = binding.tabs.getTabAt(1)!!.orCreateBadge
        badge.isVisible = false
        badge2.isVisible = false
        viewModel.received.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Success -> {
                    if(it.data.isNotEmpty()){
                        badge.isVisible = true
                        badge.number = it.data.size
                    }else{
                        badge.isVisible = false
                    }
                }
            }
        })

        viewModel.sent.observe(viewLifecycleOwner, Observer {
            when (it){
                is Resource.Success -> {
                    if(it.data.isNotEmpty()){
                        badge2.isVisible = true
                        badge2.number = it.data.size
                    }else{
                        badge2.isVisible = false
                    }
                }
            }
        })
    }


    override fun getLayoutRes(): Int = R.layout.fragment_requests

}
