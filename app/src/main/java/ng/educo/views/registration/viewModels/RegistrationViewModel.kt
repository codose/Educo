package ng.educo.views.registration.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.di.scope.ActivityScope
import ng.educo.models.User
import ng.educo.utils.Resource
import javax.inject.Inject


@ActivityScope
class RegistrationViewModel @Inject constructor() : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(job + Dispatchers.Main)
    val registration = MutableLiveData<Resource<Boolean>>()
    val login = MutableLiveData<Resource<Boolean>>()
    val appUser = MutableLiveData<Resource<User>>()
    val fireStoreCreate = MutableLiveData<Resource<String>>()

    @Inject
    lateinit var userRepo : FirebaseRepository


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

    fun addToFireStore(user : User){
        fireStoreCreate.value = Resource.Loading()
        uiScope.launch {
            fireStoreCreate.value = withContext(Dispatchers.IO){
                userRepo.updateUser(user)
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}