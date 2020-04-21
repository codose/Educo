package ng.educo.views.main.viewmodels

import android.content.Context
import android.net.Uri
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

    val userImageUri = MutableLiveData<Uri>()

    val uploadImage = MutableLiveData<Resource<String>>()

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

    fun uploadImage(context : Context){
        uploadImage.value = Resource.Loading()
        App.applicationScope.launch {
            withContext(Dispatchers.IO){
                uploadImage.postValue(firebaseRepository.uploadImage(userImageUri.value!!, context))
            }
        }
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