package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.models.*
import ng.educo.utils.App
import ng.educo.utils.Resource
import javax.inject.Inject

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
class RequestStudyViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    val onCreateRequest = MutableLiveData<Resource<Boolean>>()

    var data = MutableLiveData<Resource<Request>>()

    var actives = MutableLiveData<Resource<List<Active>>>()

    val userDetails = MutableLiveData<Resource<User>>()

    var messages = MutableLiveData<Resource<List<Message>>>()

    val deleted = MutableLiveData<Resource<String>>()

    private val scope = App.applicationScope

    var msgSent = MutableLiveData<Resource<String>>()


    fun createNewRequest(educo: Educo){
        onCreateRequest.value = Resource.Loading()
        App.applicationScope.launch {
            onCreateRequest.value = withContext(Dispatchers.IO){
                firebaseRepository.createNewRequest(educo)
            }
        }
    }


    fun getRequests(id: String){
        data.value = Resource.Loading()
        App.applicationScope.launch {
            data.value = withContext(Dispatchers.IO){
                firebaseRepository.getRequest(id)
            }
        }
    }

    fun sendMessage(docId : String, message: Message, user : User){
        msgSent.value = Resource.Loading()
        App.applicationScope.launch {
            msgSent.value = withContext(Dispatchers.IO){
                firebaseRepository.sendMessage(docId , message, user)
            }
        }
    }

    fun getUserDetails(uid: String){
        userDetails.value = Resource.Loading()
        scope.launch {
            withContext(Dispatchers.IO){
                val user = firebaseRepository.getOtherUser(uid)
                userDetails.postValue(user)
            }
        }
    }

    fun deleteRequest(request: Request){
        deleted.value = Resource.Loading()
        App.applicationScope.launch {
            deleted.value = withContext(Dispatchers.IO){
                firebaseRepository.deleteReceivedRequests(request)
            }
        }
    }

    fun getActiveChats() {
        actives.value = Resource.Loading()
        scope.launch {
            withContext(Dispatchers.IO){
                firebaseRepository.getActiveChats().collect {
                    actives.postValue(it)
                }
            }
        }
    }

    fun getMessageFlow(docId: String){
        messages.value = Resource.Loading()
        scope.launch {
            withContext(Dispatchers.IO){
                firebaseRepository.getMessagesFlow(docId).collect {
                    messages.postValue(it)
                }
            }
        }
    }

}