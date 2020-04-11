package ng.educo.views.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import ng.educo.DataStoreArchitecture.UserRepo
import ng.educo.utils.App

class CategoryViewModel(
    private val adapter: InterestsAdapter,
    private val recyclerView: RecyclerView) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val appUser = App.appUser
    val completed = MutableLiveData<Boolean>()
    val list = MutableLiveData<List<String>>()
    lateinit var tracker : SelectionTracker<Long>
    val selectedList = MutableLiveData<ArrayList<Long>>()
    init {
        list.value = listOf("Technology","Art","Science","Law","Social Science","Architecture","Economics","Clinical Science","Education","Agricuture","Pharmacy")
        initTracker()
        completed.value = false
    }

    private fun initTracker() {
        tracker = SelectionTracker.Builder<Long>("mySelection", recyclerView,
            MyItemKeyProvider(recyclerView), MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        val ghostKey = -111L
        tracker.select(ghostKey)
        tracker.hasSelection()

        tracker.addObserver( object : SelectionTracker.SelectionObserver<Long>(){
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                selectedList.value = tracker.selection.toList() as ArrayList<Long>
                selectedList.value?.remove(-111L)
                appUser?.interest = selectedList.value as List<Long>
            }
        })
    }


    fun onProfileComplete(){
        uiScope.launch {
            updateProfile()
        }
    }
    private fun navigateToNextScreen(){
        completed.postValue(true)
    }

    fun navigationCompleted(){
        completed.value = false
    }

    private suspend fun updateProfile() {
        withContext(Dispatchers.IO){
            appUser?.accountSetup = 1
            UserRepo().updateUser(appUser!!)
            navigateToNextScreen()
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}