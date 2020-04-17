package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.models.Educo
import ng.educo.utils.App
import ng.educo.utils.Resource
import java.lang.Exception
import javax.inject.Inject

class MainViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository)  : ViewModel(){

    val loggedOut = MutableLiveData<Boolean>()
    val studyData = MutableLiveData<Resource<List<Educo>>>()

    init {
        loggedOut.value = false
    }

    fun getUsers(){
        studyData.value = Resource.Loading()
        App.applicationScope.launch {
            withContext(Dispatchers.IO){
                val data = firebaseRepository.getAllStudyPartners()
                studyData.postValue(data)
            }
        }
    }


    fun logOut(){
            firebaseRepository.logOut()
            App.appUser = null
            loggedOut.postValue(true)
        }

    fun loggedOutComplete(){
        loggedOut.value = false
    }
}