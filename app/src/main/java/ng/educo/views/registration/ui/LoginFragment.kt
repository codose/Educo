package ng.educo.views.registration.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_login.*

import ng.educo.R
import ng.educo.databinding.FragmentLoginBinding
import ng.educo.models.User
import ng.educo.views.base.BaseFragment
import ng.educo.views.categories.CategoryActivity
import ng.educo.views.main.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private lateinit var email : String
    private lateinit var password : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_login,container,false)
        binding.frgmentLoginRegisterButton.setOnClickListener {
            openRegister()
        }
        binding.fragmentLoginButton.setOnClickListener {
            if(checkNetworkState()){
                loginUser()
            }
        }
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
           login()
        }
    }

    private fun login() {
        buttonNotEnabled()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful){
                updateAppUser()
            }else{
                showToast("${it.exception?.message}")
                buttonEnabled()
            }
        }
    }

    private fun updateAppUser() {
        userRef.document(auth.currentUser!!.uid).get().addOnCompleteListener {
            if(it.isSuccessful){
                val user = it.result?.toObject<User>()
                setAppUser(user!!)
                buttonEnabled()
                val intent = Intent(context,CategoryActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
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
