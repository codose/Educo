package ng.educo.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import ng.educo.models.User

class App : Application() {

    lateinit var firestore: FirebaseFirestore
    private lateinit var appUser : User

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firestore = Firebase.firestore
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
    }

    fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getAppUser() : User{
        return appUser
    }

    fun setAppUser(newUser : User){
        appUser = newUser
    }

}