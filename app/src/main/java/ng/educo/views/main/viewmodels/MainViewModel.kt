package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.di.scope.ActivityScope
import ng.educo.models.Educo
import ng.educo.models.Request
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton



class MainViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository)  : ViewModel(){

    val loggedOut = MutableLiveData<Boolean>()
    val requestSent = MutableLiveData<Resource<String>>()
    val received = MutableLiveData<Resource<List<Request>>>()
    val sent = MutableLiveData<Resource<List<Request>>>()
    val studyPartnerData = MutableLiveData<Resource<List<Educo>>>()
    val studyGroupData = MutableLiveData<Resource<List<Educo>>>()
    val studyGroupSingleData = MutableLiveData<Resource<Educo>>()
    val userDetails = MutableLiveData<Resource<User>>()
    private val applicationScope = App.applicationScope


    init {
        loggedOut.value = false
        getStudyGroupData()
        getStudyPartnerData()
        getReceivedDetails()
        getSentDetails()
    }

    fun getStudyPartnerData(){
        studyPartnerData.value = Resource.Loading()
        applicationScope.launch {
            withContext(Dispatchers.IO){
                val data = firebaseRepository.getAllStudyPartners()
                studyPartnerData.postValue(data)
            }
        }
    }

    fun sendRequest(receiver:String, request : Request){
        requestSent.value = Resource.Loading()
        applicationScope.launch {
            withContext(Dispatchers.IO){
                requestSent.postValue(firebaseRepository.applyForStudy(receiver,request))
            }
        }
    }


    fun getUserDetails(uid: String){
        userDetails.value = Resource.Loading()
        applicationScope.launch {
            withContext(Dispatchers.IO){
                val user = firebaseRepository.getOtherUser(uid)
                userDetails.postValue(user)
            }
        }
    }

    fun getStudyGroupData(){
        studyGroupData.value = Resource.Loading()
        applicationScope.launch {
            withContext(Dispatchers.IO){
                val data = firebaseRepository.getAllStudyGroups()
                studyGroupData.postValue(data)
            }
        }
    }

    fun getSingleEduco(id : String){

        studyGroupSingleData.value = Resource.Loading()

        applicationScope.launch {
            withContext(Dispatchers.IO){
                studyGroupSingleData.postValue(firebaseRepository.getSingleStudy(id))
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

    private fun getReceivedDetails() {
        received.value = Resource.Loading()
        applicationScope.launch {
            withContext(Dispatchers.IO){
                received.postValue(firebaseRepository.getReceivedRequests())
            }
        }
    }

    private fun getSentDetails() {
        sent.value = Resource.Loading()
        applicationScope.launch {
            withContext(Dispatchers.IO){
                sent.postValue(firebaseRepository.getSentRequests())
            }
        }
    }
}