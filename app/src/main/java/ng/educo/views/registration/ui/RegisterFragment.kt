package ng.educo.views.registration.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.views.base.BaseFragment
import ng.educo.R
import ng.educo.databinding.FragmentRegisterBinding
import ng.educo.models.User
import ng.educo.utils.Resource
import ng.educo.views.categories.CategoryActivity
import ng.educo.views.registration.RegistrationActivity
import ng.educo.views.registration.viewModels.RegistrationViewModel
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private lateinit var firstName :String
    private lateinit var lastName :String
    private lateinit var email :String
    private lateinit var phone :String
    private lateinit var password :String
    private lateinit var confirmPassword :String
    private var accountSetup : Int = 0
    private lateinit var interests : List<Long>
    private lateinit var newUser : User

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.registerProgress.indeterminateDrawable = doubleBounce

        binding.fragmentRegisterButton.setOnClickListener {
            if(checkNetworkState()){
                getEditValues()
            }
        }

        viewModel.registration.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> disableButton()

                is Resource.Success -> {
                    newUser.uid = auth.currentUser!!.uid
                    viewModel.addToFireStore(newUser)
                }

                is Resource.Failure -> {
                    enableButton()
                    showToast(it.message)
                }
            }
        })

        viewModel.fireStoreCreate.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> disableButton()

                is Resource.Success -> {
                    setAppUser(newUser)
                    showToast("Account successfully created")
                    val intent = Intent(context, CategoryActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                }

                is Resource.Failure -> {
                    enableButton()
                    showToast(it.message)
                }
            }
        })

        return binding.root
    }

    private fun disableButton() {
        binding.fragmentRegisterButton.apply {
            isEnabled = false
            text = ""
        }
        binding.registerProgress.visibility = View.VISIBLE
    }
    private fun enableButton() {
        binding.fragmentRegisterButton.apply {
            isEnabled = true
            text = resources.getText(R.string.register)
        }
        binding.registerProgress.visibility = View.GONE

    }

    private fun getEditValues(){
        firstName = binding.frgmentRegisterFirstNameEditText.text.toString()
        lastName = binding.fragmentRegisterLastNameEditText.text.toString()
        email = binding.frgmentRegisterEmailEditText.text.toString()
        password = binding.frgmentRegisterPasswordEditText.text.toString()
        confirmPassword = binding.frgmentRegisterConfirmPasswordEditText.text.toString()
        phone = binding.frgmentRegisterPhoneEditText.text.toString()
        interests  = mutableListOf()

        if(!validateInputs()){
            enableButton()
            return
        }else{
            binding.registerProgress.visibility = View.VISIBLE
            newUser = User(firstName, lastName, email, phone, accountSetup, null,
                interests as MutableList<Long>
            )
            viewModel.registerUser(email,password)
        }
    }


    private fun validateInputs(): Boolean {
        if (!isValidUserName(firstName.trim())) {
            binding.firstNameTextInputLayout.error = "First name required"
            return false
        } else {
            binding.firstNameTextInputLayout.error = null
        }
        if (!isValidUserName(lastName.trim())) {
            binding.lastNameTextInputLayout.error = "Last name required"
            return false
        }else {
            binding.lastNameTextInputLayout.error = null
        }
        if (!isValidEmail(email)) {
            binding.emailNameTextInputLayout.error = "Email Required"
            return false
        }else {
            binding.emailNameTextInputLayout.error = null
        }
        if (!isValidPhoneNo(phone.trim())) {
            binding.phoneTextInputLayout.error = "valid phone number Required"
            return false
        }else {
            binding.phoneTextInputLayout.error = null
        }
        if (!isValidPassword(password.trim())) {
            binding.passwordTextInputLayout.error = "Password of 8 characters & above Required"
            return false
        }else {
            binding.passwordTextInputLayout.error = null
        }
        if (!isValidConfirmPassword()) {
            binding.confirmPasswordTextInputLayout.error = "Confirm Password not match"
            return false
        }else {
            binding.confirmPasswordTextInputLayout.error = null
        }
        return true
    }
    private fun isValidConfirmPassword(): Boolean {
        return password == confirmPassword
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_register
    }
}
