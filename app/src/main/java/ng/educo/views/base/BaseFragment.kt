package ng.educo.views.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.github.ybq.android.spinkit.style.FadingCircle
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import ng.educo.R
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Constants.COLLECTION_USERS
import java.util.regex.Pattern

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseFragment<DB :ViewDataBinding> : Fragment(){
    open lateinit var binding: DB
    lateinit var auth: FirebaseAuth
    lateinit var doubleBounce : FadingCircle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(context!!)
        auth = FirebaseAuth.getInstance()
        doubleBounce = FadingCircle()
        doubleBounce.color = ContextCompat.getColor(context!!, R.color.colorPrimary)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init(inflater, container)
        super.onCreateView(inflater, container, savedInstanceState)
        return binding.root
    }

    private fun init(inflater : LayoutInflater, container : ViewGroup?){
        binding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
    }

    @LayoutRes
    abstract fun getLayoutRes(): Int

    fun showToast(message : String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(message: String){
        view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_LONG).show()
        }
    }

    fun setAppUser(user : User){
        App().setAppUser(user)
    }

    fun getAppUser() : User? = App.appUser

    fun checkNetworkState(): Boolean {
        if (!App().checkNetwork(context!!)) {
            showSnackBar("You do not have a network connection")
            return false
        }
        return true
    }



    fun isValidUserName(userName: String): Boolean {
        return userName.isNotEmpty()
    }

    fun isValidEmail(email: String): Boolean {
        val regex = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isValidPhoneNo(phoneNo: String?): Boolean {
        return phoneNo != null && phoneNo.length >= 10
    }

    fun isValidPassword(pass: String?): Boolean {
        return pass != null && pass.length >= 8
    }

}