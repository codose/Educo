package ng.educo.views.main.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.models.Educo
import ng.educo.utils.App
import ng.educo.utils.Resource
import javax.inject.Inject

class RequestStudyViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    val onCreateRequest = MutableLiveData<Resource<Boolean>>()

    fun createNewRequest(educo: Educo){
        onCreateRequest.value = Resource.Loading()
        App.applicationScope.launch {
            onCreateRequest.value = withContext(Dispatchers.IO){
                firebaseRepository.createNewRequest(educo)
            }
        }
    }

}