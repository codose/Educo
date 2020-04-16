package ng.educo.views.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.selection.SelectionTracker
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.FirebaseRepository
import ng.educo.utils.App
import ng.educo.utils.Constants.interests
import ng.educo.utils.Resource
import javax.inject.Inject


class CategoryViewModel @Inject constructor(private val firebaseRepository: FirebaseRepository) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    private val appUser = App.appUser
    val completed = MutableLiveData<Resource<Boolean>>()
    val list = MutableLiveData<List<String>>()
    val selectedList = MutableLiveData<MutableList<Long>>()
    init {
        list.value = interests
        selectedList.value = mutableListOf()
    }



    fun onProfileComplete(){
        uiScope.launch {
            completed.value = Resource.Loading()
            completed.value = withContext(Dispatchers.IO){
                appUser?.accountSetup = 1
                appUser?.interest = selectedList.value!!
                firebaseRepository.updateUser(appUser!!)
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}