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
import ng.educo.di.components.AppComponent
import ng.educo.di.components.DaggerAppComponent
import ng.educo.models.User

open class App : Application() {


    companion object {
        lateinit var firestore: FirebaseFirestore
        var appUser: User? = null
        var job = Job()
        var applicationScope = CoroutineScope(job + Dispatchers.Main)
    }

    val appComponent : AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }


    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        firestore = Firebase.firestore
        firestore.firestoreSettings = firestoreSettings {
            isPersistenceEnabled = true
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

    private fun initPrefLib() {
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