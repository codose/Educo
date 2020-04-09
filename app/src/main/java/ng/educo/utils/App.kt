package ng.educo.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import ng.educo.models.User

class App : Application() {

    private lateinit var appUser : User

    override fun onCreate() {
        super.onCreate()
    }

    public fun checkNetwork(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    public fun getAppUser() : User{
        return appUser
    }

    fun setAppUser(newUser : User){
        appUser = newUser
    }

}