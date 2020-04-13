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
import ng.educo.utils.Resource
import java.lang.Exception
import kotlin.Long as Long1

class CategoryViewModel(
    private val adapter: InterestsAdapter,
    private val recyclerView: RecyclerView) : ViewModel() {
    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    private val userRepo = UserRepo()

    private val appUser = App.appUser
    val completed = MutableLiveData<Resource<Boolean>>()
    val list = MutableLiveData<List<String>>()
    lateinit var tracker : SelectionTracker<Long1>
    val selectedList = MutableLiveData<MutableList<Long1>>()
    init {
        list.value = listOf("Technology","Art","Science","Law","Social Science","Architecture","Economics","Clinical Science","Education","Agricuture","Pharmacy")
        selectedList.value = mutableListOf()
        initTracker()
    }

    private fun initTracker() {
        tracker = SelectionTracker.Builder<Long1>("mySelection", recyclerView,
            MyItemKeyProvider(recyclerView), MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()
        val ghostKey = -111L
        tracker.select(ghostKey)
        tracker.hasSelection()

        tracker.addObserver( object : SelectionTracker.SelectionObserver<Long1>(){
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                selectedList.value = tracker.selection.toMutableList()
                selectedList.value?.remove(ghostKey)
                appUser?.interest = selectedList.value!!
            }
        })
    }


    fun onProfileComplete(){
        uiScope.launch {
            completed.value = Resource.Loading()
            completed.value = withContext(Dispatchers.IO){
                appUser?.accountSetup = 1
                userRepo.updateUser(appUser!!)
            }
        }
    }



    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}