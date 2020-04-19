package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton


class ProfileViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel(){

    val getUserProfile = MutableLiveData<User>()

    var userDataUpdated = MutableLiveData<Resource<String>>()

    init {
        getUserProfile.value = App.appUser
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