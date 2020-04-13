package ng.educo.views.registration.viewModels

import android.app.Application
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BaseObservable
import androidx.databinding.Observable
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.UserRepo
import ng.educo.databinding.FragmentRegisterBinding
import ng.educo.models.User
import ng.educo.utils.Resource

class RegistrationViewModel : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)
    val registration = MutableLiveData<Resource<Boolean>>()
    val login = MutableLiveData<Resource<Boolean>>()
    val appUser = MutableLiveData<Resource<User>>()
    val firestoreCreate = MutableLiveData<Resource<Boolean>>()
    private val userRepo = UserRepo()


    fun registerUser(email: String, password : String){
        registration.value = Resource.Loading()
        uiScope.launch {
            registration.value = withContext(Dispatchers.IO){
                userRepo.registerUser(email,password)
            }
        }
    }

    fun getUser(){
        appUser.value = Resource.Loading()
        uiScope.launch {
            appUser.value = withContext(Dispatchers.IO){
                userRepo.getUser()
            }
        }
    }

    fun loginUser(email: String, password : String){
        login.value = Resource.Loading()
        uiScope.launch {
            login.value = withContext(Dispatchers.IO){
                userRepo.loginUser(email,password)
            }
        }
    }

    fun addToFirestore(user : User){
        firestoreCreate.value = Resource.Loading()
        uiScope.launch {
            firestoreCreate.value = withContext(Dispatchers.IO){
                userRepo.updateUser(user)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}