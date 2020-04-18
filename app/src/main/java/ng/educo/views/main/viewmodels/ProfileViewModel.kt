package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.models.User
import ng.educo.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton


class ProfileViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel(){

    val getUserProfile = MutableLiveData<Resource<User>>()

    val job = Job()
    val uiScope = CoroutineScope(job + Dispatchers.Main)
    init {
        getUserData()
    }

    private fun getUserData(){
        getUserProfile.value = Resource.Loading()
        uiScope.launch {
            withContext(Dispatchers.IO){
                getUserProfile.postValue(firebaseRepository.getUser())
            }
        }
    }



}