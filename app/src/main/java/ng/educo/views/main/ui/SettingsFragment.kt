package ng.educo.views.main.ui

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

import ng.educo.R
import ng.educo.databinding.FragmentSettingsBinding
import ng.educo.views.base.BaseFragment
import ng.educo.views.main.viewmodels.MainViewModel
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
        val bottomNavigationView : BottomNavigationView = activity!!.findViewById(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
        val fab = activity?.findViewById<FloatingActionButton>(R.id.search_new_btn)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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

        return binding.root
    }

    override fun getLayoutRes(): Int = R.layout.fragment_settings

}
