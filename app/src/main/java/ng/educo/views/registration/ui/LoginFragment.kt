package ng.educo.views.registration.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController

import ng.educo.R
import ng.educo.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding : FragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        binding.frgmentLoginRegisterButton.setOnClickListener {
            openRegister()
        }

        return binding.root
    }

    private fun openRegister(){
        this.findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToRegisterFragment())
    }

}
