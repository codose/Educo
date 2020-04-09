package ng.educo.views.registration.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ng.educo.views.BaseFragment
import ng.educo.R
import ng.educo.databinding.FragmentRegisterBinding
import ng.educo.models.User
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    private lateinit var firstName :String
    private lateinit var lastName :String
    private lateinit var email :String
    private lateinit var phone :String
    private lateinit var password :String
    private lateinit var confirmPassword :String

    private lateinit var newUser : User



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false)
        binding.fragmentRegisterButton.setOnClickListener {

            if(checkNetworkState()){
                binding.fragmentRegisterButton.apply {
                    isEnabled = false
                    text = ""
                }
                getEditValues()
            }

        }
        return binding.root
    }

    private fun getEditValues(){
        firstName = binding.frgmentRegisterFirstNameEditText.text.toString()
        lastName = binding.fragmentRegisterLastNameEditText.text.toString()
        email = binding.frgmentRegisterEmailEditText.text.toString()
        password = binding.frgmentRegisterPasswordEditText.text.toString()
        confirmPassword = binding.frgmentRegisterConfirmPasswordEditText.text.toString()
        phone = binding.frgmentRegisterPhoneEditText.text.toString()

        newUser = User(firstName, lastName, email, phone)

        if(!validateInputs()){
            binding.fragmentRegisterButton.apply {
                isEnabled = true
                text = resources.getText(R.string.register)
            }
            return
        }else{
            binding.registerProgress.visibility = View.VISIBLE
            newUser = User(firstName, lastName, email, phone)
            registerUser(newUser)
        }

    }

    private fun registerUser(newUser: User) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    saveUserToFireStore(newUser)
                } else {
                    binding.fragmentRegisterButton.apply {
                        isEnabled = true
                        text = resources.getText(R.string.register)
                    }
                    binding.registerProgress.visibility = View.GONE
                    showToast("Unable to register user + {${it.exception.toString()}}")
                }
            }
    }

    private fun saveUserToFireStore(user : User) {
        userRef.document(auth.currentUser!!.uid).set(user).addOnCompleteListener {
            if(it.isSuccessful){
                binding.fragmentRegisterButton.apply {
                    isEnabled = true
                    text = resources.getText(R.string.register)
                }
                binding.registerProgress.visibility = View.GONE
                setAppUser(user)
                this.findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment2())
            }
            else{
                binding.registerProgress.visibility = View.GONE
                binding.fragmentRegisterButton.apply {
                    isEnabled = true
                    text = resources.getText(R.string.register)
                    showToast("Error Occurred")
                }
            }
        }
    }


    private fun validateInputs(): Boolean {
        if (!isValidUserName(firstName)) {
            binding.firstNameTextInputLayout.error = "First name required"
            return false
        }
        if (!isValidUserName(lastName)) {
            binding.lastNameTextInputLayout.error = "Last name required"
            return false
        }
        if (!isValidEmail(email)) {
            binding.emailNameTextInputLayout.error = "Email Required"
            return false
        }
        if (!isValidPhoneNo(phone)) {
            binding.phoneTextInputLayout.error = "valid phone number Required"
            return false
        }
        if (!isValidPassword(password)) {
            binding.passwordTextInputLayout.error = "Password of 8 characters & above Required"
            return false
        }
        if (!isValidConfirmPassword()) {
            binding.confirmPasswordTextInputLayout.error = "Confirm Password not match"
            return false
        }
        return true
    }

    private fun isValidUserName(userName: String): Boolean {
        return userName.isNotEmpty()
    }

    private fun isValidEmail(email: String): Boolean {
        val regex = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    private fun isValidPhoneNo(phoneNo: String?): Boolean {
        return phoneNo != null && phoneNo.length >= 10
    }

    private fun isValidPassword(pass: String?): Boolean {
        return pass != null && pass.length >= 8
    }

    private fun isValidConfirmPassword(): Boolean {
        return password == confirmPassword
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_register
    }

}
