package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.models.Educo
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton


class ProfileViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel(){

    val getUserProfile = MutableLiveData<User>()

    var myPartners = MutableLiveData<Resource<List<Educo>>>()


    var userDataUpdated = MutableLiveData<Resource<String>>()

    init {
        App.applicationScope.launch {
            withContext(Dispatchers.IO){
                val userResource = firebaseRepository.getUser()
                App.appUser = when(userResource){
                    is Resource.Success -> userResource.data
                    else -> null
                }
                getUserProfile.postValue(App.appUser)
            }
        }
        getStudyCount()
    }



    fun getStudyCount(){
        App.applicationScope.launch {
            withContext(Dispatchers.IO){
                myPartners.postValue(firebaseRepository.getMyStudies())
            }
        }
    }

    fun updateUser(user: User){
        userDataUpdated.value = Resource.Loading()
        App.applicationScope.launch {
            withContext(Dispatchers.IO){
                userDataUpdated.postValue(firebaseRepository.updateUser(user))
            }
        }
    }
}