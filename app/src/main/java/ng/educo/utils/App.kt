package ng.educo.utils

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.UserRepo
import ng.educo.models.User

class App : Application() {


    companion object {
        lateinit var firestore: FirebaseFirestore
        var appUser: User? = null
        var job = Job()
        var applicationScope = CoroutineScope(job + Dispatchers.Main)
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firestore = Firebase.firestore
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
        }
        val userRepo = UserRepo()
        applicationScope.launch {
            withContext(Dispatchers.IO){
                appUser = userRepo.getUser()
            }
        }
        initPrefLib()
    }

    fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getAppUser(): User? {
        return appUser
    }

    fun initPrefLib() {
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

    fun setAppUser(appUser: User) {
        App.appUser = appUser
    }
}