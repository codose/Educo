package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.models.*
import ng.educo.utils.App
import ng.educo.utils.Resource
import javax.inject.Inject

class RequestStudyViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    val onCreateRequest = MutableLiveData<Resource<Boolean>>()
    var data = MutableLiveData<Resource<Request>>()
    var actives = MutableLiveData<Resource<List<Active>>>()

    val deleted = MutableLiveData<Resource<String>>()


    val scope = App.applicationScope

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

    fun deleteRequest(request: Request){
        deleted.value = Resource.Loading()
        App.applicationScope.launch {
            deleted.value = withContext(Dispatchers.IO){
                firebaseRepository.deleteReceivedRequests(request)
            }
        }
    }

    fun getMessages() {
        actives.value = Resource.Loading()
        scope.launch {
            actives.value = withContext(Dispatchers.IO){
                firebaseRepository.getActiveChats(App.appUser!!.uid)
            }
        }
    }
}