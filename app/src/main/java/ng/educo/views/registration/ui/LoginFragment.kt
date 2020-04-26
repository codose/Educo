package ng.educo.views.registration.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

import ng.educo.R
import ng.educo.databinding.FragmentLoginBinding
import ng.educo.utils.App
import ng.educo.utils.Resource
import ng.educo.views.base.BaseFragment
import ng.educo.views.categories.CategoryActivity
import ng.educo.views.main.MainActivity
import ng.educo.views.registration.RegistrationActivity
import ng.educo.views.registration.viewModels.RegistrationViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var email : String
    private lateinit var password : String

    @Inject
    lateinit var viewModel : RegistrationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity() as RegistrationActivity).registrationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        //viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        binding.loginBar.indeterminateDrawable = doubleBounce
        binding.lifecycleOwner = viewLifecycleOwner

        binding.frgmentLoginRegisterButton.setOnClickListener {
            openRegister()
        }

        binding.fragmentLoginButton.setOnClickListener {
            if(checkNetworkState()){
                loginUser()
            }
        }

        viewModel.login.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> buttonNotEnabled()

                is Resource.Success -> viewModel.getUser()

                is Resource.Failure -> {
                    showToast(it.message)
                    buttonEnabled()
                }
            }
        })

        viewModel.appUser.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> buttonNotEnabled()

                is Resource.Success -> {
                    setAppUser(it.data)
                    if(App.appUser?.accountSetup == 0){
                        val intent = Intent(context,CategoryActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }else{
                        val intent = Intent(context,MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }

                is Resource.Failure -> {
                    showToast(it.message)
                    buttonEnabled()
                }
            }
        })


        return binding.root
    }


    private fun buttonEnabled(){
        binding.apply {
            loginBar.visibility = View.GONE
            fragmentLoginButton.isEnabled = true
            frgmentLoginRegisterButton.isEnabled = true
        }
    }

    private fun buttonNotEnabled(){
        binding.apply {
            loginBar.visibility = View.VISIBLE
            fragmentLoginButton.isEnabled = false
            frgmentLoginRegisterButton.isEnabled = false
        }
    }

    private fun loginUser() {
        email = binding.fragmentLoginEmailEditText.text.toString()
        password = binding.fragmentLoginPasswordEditText.text.toString()
        if(validateInputs()){
            viewModel.loginUser(email, password)
        }
    }

    private fun validateInputs(): Boolean {
        if (!isValidEmail(email)) {
            binding.emailTextInputLayout.error = "Email Required"
            return false
        }
        if (!isValidPassword(password)) {
            binding.passwordTextInputLayout.error = "Password of 8 characters & above Required"
            return false
        }
        return true
    }

    private fun openRegister(){
        this.findNavController().navigate(LoginFragmentDirections.actionLoginFragment2ToRegisterFragment())
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_login
    }

}
