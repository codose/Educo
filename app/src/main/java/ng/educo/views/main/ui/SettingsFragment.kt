package ng.educo.views.main.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.educo.DataStoreArchitecture.UserRepo

import ng.educo.R
import ng.educo.databinding.FragmentSettingsBinding
import ng.educo.utils.App
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.MainViewModel
import ng.educo.views.registration.RegistrationActivity

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = DataBindingUtil.inflate(inflater, R.layout.fragment_settings,container,false)

        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel.loggedOut.observe(viewLifecycleOwner, Observer{
            if(it){
                startActivity(Intent(context, RegistrationActivity::class.java))
                activity!!.finish()
                showToast("Logged Out")
                viewModel.loggedOutComplete()
            }
        })

        return binding.root
    }

    override fun getLayoutRes(): Int = R.layout.fragment_settings

}
