package ng.educo.DataStoreArchitecture

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Dispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Constants

class FireBaseUtil {
    val database = Firebase.firestore
    var userRef: CollectionReference
    var auth : FirebaseAuth
    var app : App

    init {
        userRef = database.collection(Constants.COLLECTION_USERS)
        auth = FirebaseAuth.getInstance()
        app = App()
    }

    suspend fun updateUserProfile(user : User){
        withContext(Dispatchers.IO){
            userRef.document(auth.currentUser!!.uid).set(user, SetOptions.merge()).await()
        }

    }
}