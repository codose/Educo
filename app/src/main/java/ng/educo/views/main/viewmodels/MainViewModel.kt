package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.utils.App
import java.lang.Exception

class MainViewModel  : ViewModel(){
    val loggedOut = MutableLiveData<Boolean>()
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    init {
        loggedOut.value = false
    }

    fun logOut(){
        uiScope.launch {
            withContext(Dispatchers.IO){
                try{
                    FirebaseRepository().logOut()
                    App.appUser = null
                    loggedOut.postValue(true)
                }catch (e : Exception){

                }
            }
        }
    }

    fun loggedOutComplete(){
        loggedOut.value = false
    }
}