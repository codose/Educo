package ng.educo.DataStoreArchitecture

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import ng.educo.models.User
import ng.educo.utils.App
import ng.educo.utils.Constants

class UserRepo {
    val auth = FirebaseAuth.getInstance()
    val database = App().firestore
    val userRef = database.collection(Constants.COLLECTION_USERS)
    suspend fun updateUser(user : User) : Boolean{
        return try {
            userRef.document(auth.currentUser!!.uid).set(user, SetOptions.merge()).await()
            true
        } catch (e : Exception){
            false
        }
    }
}