package ng.educo.views.main.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

import ng.educo.R
import ng.educo.databinding.FragmentSettingsBinding
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainActivity
import ng.educo.views.main.viewmodels.MainViewModel
import ng.educo.views.registration.RegistrationActivity
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    lateinit var viewModel : MainViewModel

    override fun onAttach(context: Context) {
            super.onAttach(context)
            (requireActivity() as MainActivity).mainComponent.inject(this)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_settings,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
        fab?.visibility = GONE
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.ProfileButton.setOnClickListener {
            this.findNavController().navigate(R.id.action_settingsFragment_to_profileFragment)
        }
        viewModel.loggedOut.observe(viewLifecycleOwner, Observer{
            if(it){
                startActivity(Intent(context, RegistrationActivity::class.java))
                activity!!.finish()
                showToast("Logged Out")
                viewModel.loggedOutComplete()
            }
        })
    }
    override fun getLayoutRes(): Int = R.layout.fragment_settings

}
